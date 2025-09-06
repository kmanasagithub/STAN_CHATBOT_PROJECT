package com.chatbot.llm;

import com.chatbot.model.entity.ConversationSummary;
import com.chatbot.model.entity.UserPreference;
import com.chatbot.model.entity.UserProfile;

import java.util.List;
import java.util.StringJoiner;

/**
 * Creates the "system" prompt that steers the assistant:
 * - human-like, empathetic, and contextual
 * - remembers real facts users told (no fake memories)
 * - adapts tone while staying safe and consistent
 */
public class PromptBuilder {

    public static String buildSystemPrompt(UserProfile profile,
                                           List<UserPreference> prefs,
                                           ConversationSummary summary,
                                           String tone) {
        StringJoiner j = new StringJoiner("\n");

        j.add("You are 'Nova', a friendly social-chat companion in a UGC app.");
        j.add("Core rules:");
        j.add("- Be warm, concise, and human-like. Avoid robotic text.");
        j.add("- Match the user's emotional tone (" + tone + ") but remain supportive.");
        j.add("- Use ONLY facts the user told you earlier; do NOT invent memories.");
        j.add("- If unsure about any 'past event', say you don't recall and invite clarification.");
        j.add("- Keep identity consistent (name: Nova; role: chat companion).");

        if (profile != null) {
            j.add("\nKnown profile:");
            if (profile.getName() != null) j.add(" - name: " + profile.getName());
            if (profile.getLocation() != null) j.add(" - location: " + profile.getLocation());
            if (profile.getStyle() != null) j.add(" - style: " + profile.getStyle());
        }

        if (prefs != null && !prefs.isEmpty()) {
            j.add("\nStable preferences heard earlier:");
            for (UserPreference p : prefs) {
                j.add(" - " + p.getKeyName() + ": " + p.getValueText());
            }
        }

        if (summary != null && summary.getSummary() != null && !summary.getSummary().isBlank()) {
            j.add("\nConversation so far (summary): " + summary.getSummary());
        }

        j.add("\nBehavior details:");
        j.add("- If user greets repeatedly, vary your greetings to avoid repetition.");
        j.add("- If user expresses sadness/anger, acknowledge feelings empathetically.");
        j.add("- If user switches mood mid-chat, gently adapt to the new tone.");
        j.add("- Offer small callbacks (e.g., “you mentioned…”), only if truly said earlier.");

        return j.toString();
    }
}
