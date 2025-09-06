package com.chatbot.model.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;   // who this conversation belongs to
    private String role;     // "user" or "assistant"
    @Column(length = 4000)
    private String content;
    private Instant createdAt = Instant.now();

    public ChatMessage() {}
    public ChatMessage(String userId, String role, String content) {
        this.userId = userId; this.role = role; this.content = content;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRole(String role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
