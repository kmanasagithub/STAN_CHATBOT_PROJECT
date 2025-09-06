package com.chatbot.model.entity;

import jakarta.persistence.*;

@Entity
public class ConversationSummary {
    @Id
    private String userId; // one rolling summary per user

    @Column(length = 6000)
    private String summary; // short evolving abstract of history

    public ConversationSummary() {}
    public ConversationSummary(String userId, String summary) {
        this.userId = userId; this.summary = summary;
    }
    public String getUserId() { return userId; }
    public String getSummary() { return summary; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setSummary(String summary) { this.summary = summary; }
}
