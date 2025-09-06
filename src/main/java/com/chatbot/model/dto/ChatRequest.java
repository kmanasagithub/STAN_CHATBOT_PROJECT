package com.chatbot.model.dto;

public class ChatRequest {
    private String userId;
    private String message;

    public ChatRequest() {}
    public ChatRequest(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setMessage(String message) { this.message = message; }
}

