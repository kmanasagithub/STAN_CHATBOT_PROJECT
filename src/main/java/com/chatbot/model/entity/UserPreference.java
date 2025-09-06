package com.chatbot.model.entity;

import jakarta.persistence.*;

@Entity
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String keyName;    // e.g., "favorite_color"
    private String valueText;  // e.g., "blue"

    public UserPreference() {}
    public UserPreference(String userId, String keyName, String valueText) {
        this.userId = userId; this.keyName = keyName; this.valueText = valueText;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getKeyName() { return keyName; }
    public String getValueText() { return valueText; }
    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setKeyName(String keyName) { this.keyName = keyName; }
    public void setValueText(String valueText) { this.valueText = valueText; }
}
