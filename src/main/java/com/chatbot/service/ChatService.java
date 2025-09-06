package com.chatbot.service;

import com.chatbot.llm.LlmClient;
import com.chatbot.llm.PromptBuilder;
import com.chatbot.model.entity.ChatMessage;
import com.chatbot.model.entity.ConversationSummary;
import com.chatbot.model.entity.UserProfile;
import com.chatbot.model.entity.UserPreference;
import com.chatbot.service.ToneService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    private final MemoryService memory;
    private final ToneService tone;
    private final LlmClient llm;

    public ChatService(MemoryService memory, ToneService tone, LlmClient llm) {
        this.memory = memory;
        this.tone = tone;
        this.llm = llm;
    }

    public String reply(String userId, String userMessage) throws Exception {
        // 1) Save user message, update facts
        memory.recordUserMessage(userId, userMessage);

        // 2) Load memory (profile, preferences, recent history, summary)
        UserProfile profile = memory.loadOrCreateProfile(userId);
        List<UserPreference> prefs = memory.getPreferences(userId);
        ConversationSummary summary = memory.getSummary(userId);

        // 3) Detect tone
        String t = tone.detectTone(userMessage);

        // 4) Build system prompt (persona + memory + tone)
        String systemPrompt = PromptBuilder.buildSystemPrompt(profile, prefs, summary, t);

        // 5) Prepare recent conversation context (most recent first -> reverse to oldest->newest)
        List<ChatMessage> last = memory.fetchRecentMessages(userId, 12);
        Collections.reverse(last);

        List<LlmClient.Message> msgs = new ArrayList<>();
        for (ChatMessage m : last) msgs.add(new LlmClient.Message(m.getRole(), m.getContent()));

        // 6) Call LLM
        String assistant = llm.chat(msgs, systemPrompt);

        // 7) Save assistant message
        memory.recordAssistantMessage(userId, assistant);

        // 8) Refresh rolling summary using a tiny prompt (cheap)
        String newSummary = summarize(last, assistant);
        memory.upsertSummary(userId, newSummary);

        return assistant;
    }

    /**
     * Small on-the-fly summarizer (local rule-based). You can swap this to LLM summarization if you like.
     * For the assignment, a compact heuristic summary is enough to show "memory compression".
     */
    private String summarize(List<ChatMessage> recent, String assistant) {
        int countUser = (int) recent.stream().filter(m -> "user".equals(m.getRole())).count();
        int countAssistant = (int) recent.stream().filter(m -> "assistant".equals(m.getRole())).count();
        String lastUser = recent.stream().filter(m -> "user".equals(m.getRole()))
                .reduce((a, b) -> b).map(ChatMessage::getContent).orElse("");
        if (lastUser.length() > 120) lastUser = lastUser.substring(0, 120) + "...";
        String assistantClip = assistant.length() > 120 ? assistant.substring(0,120)+"..." : assistant;
        return "Exchanged " + (countUser + countAssistant) + " turns recently. " +
                "Latest user intent: \"" + lastUser + "\". " +
                "Assistant responded: \"" + assistantClip + "\".";
    }
}
