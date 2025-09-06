package com.chatbot.service;

import org.springframework.stereotype.Service;

/**
 * Tiny heuristic tone detector to guide the style of responses.
 * You can replace with an LLM classifier later if you want.
 */
@Service
public class ToneService {
    public String detectTone(String userMessage) {
        String m = userMessage.toLowerCase();
        if (m.contains("sad") || m.contains("down") || m.contains("depressed") || m.contains("heartbroken"))
            return "empathetic";
        if (m.contains("angry") || m.contains("mad") || m.contains("furious") || m.contains("roast"))
            return "edgy/playful but safe";
        if (m.contains("formal") || m.contains("official"))
            return "formal";
        return "casual";
    }
}
