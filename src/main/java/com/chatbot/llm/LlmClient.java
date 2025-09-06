package com.chatbot.llm;

import java.util.List;

/**
 * Generic interface so you can plug in Gemini, OpenAI, Claude, or local models.
 */
public interface LlmClient {
    String chat(List<Message> messages, String systemPrompt) throws Exception;

    class Message {
        public final String role;   // "user" or "assistant"
        public final String content;
        public Message(String role, String content) { this.role = role; this.content = content; }
    }
}

