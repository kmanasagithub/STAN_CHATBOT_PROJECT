package com.chatbot.llm;

import okhttp3.*;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GeminiClient implements LlmClient {
    private final String apiKey;
    private final OkHttpClient http = new OkHttpClient();
    private static final String MODEL = "gemini-1.5-flash"; // or "gemini-2.0-flash" if enabled
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public GeminiClient(@Value("${gemini.api.key}") String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("gemini.api.key not set in application.properties");
        }
        this.apiKey = apiKey;
    }

    @Override
    public String chat(List<Message> messages, String systemPrompt) throws Exception {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY not set");
        }

        // Build a single prompt that includes our "system" instructions + conversation turns
        StringBuilder userContent = new StringBuilder();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            userContent.append("[SYSTEM]\n").append(systemPrompt).append("\n\n");
        }
        for (Message m : messages) {
            userContent.append(m.role.toUpperCase()).append(": ").append(m.content).append("\n");
        }

        String json = """
            {
              "contents": [{
                "parts": [{"text": %s}]
              }]
            }
        """.formatted(quote(userContent.toString()));

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + MODEL + ":generateContent?key=" + apiKey;

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request req = new Request.Builder().url(url).post(body).build();

        try (Response resp = http.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new RuntimeException("Gemini HTTP " + resp.code() + ": " + resp.body().string());
            }

            String respBody = Objects.requireNonNull(resp.body()).string();

            // 🔹 Debug log raw response (optional)
            System.out.println("Gemini raw response: " + respBody);

            // ✅ Proper JSON parsing using Jackson
            JsonNode root = MAPPER.readTree(respBody);
            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                return "(No response)";
            }

            JsonNode parts = candidates.get(0).path("content").path("parts");
            if (!parts.isArray() || parts.isEmpty()) {
                return "(No response)";
            }

            return parts.get(0).path("text").asText("(No response)");
        }
    }

    private static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n") + "\"";
    }

    private static String unescape(String s) {
        return s.replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
