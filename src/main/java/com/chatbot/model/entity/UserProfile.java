package com.chatbot.model.entity;

import jakarta.persistence.*;

@Entity
public class UserProfile {
    @Id
    private String userId;
    private String name;       // learned name (if user tells us)
    private String location;   // learned location (e.g., “Delhi”)
    private String style;      // e.g., “casual”, “formal” (learned preference)

    public UserProfile() {}
    public UserProfile(String userId) { this.userId = userId; }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getStyle() { return style; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setStyle(String style) { this.style = style; }
}

