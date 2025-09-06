package com.chatbot.model.dto;

public class ChatResponse {
    private String reply;
    private String tone;

    public ChatResponse() {}
    public ChatResponse(String reply, String tone) {
        this.reply = reply;
        this.tone = tone;
    }
    public String getReply() { return reply; }
    public String getTone() { return tone; }
    public void setReply(String reply) { this.reply = reply; }
    public void setTone(String tone) { this.tone = tone; }
}
