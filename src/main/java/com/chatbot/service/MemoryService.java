package com.chatbot.service;

import com.chatbot.model.entity.*;
import com.chatbot.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all persistent memory:
 * - user profile fields (name/location/style)
 * - stable preferences (key-value)
 * - last messages
 * - rolling conversation summary
 */
@Service
public class MemoryService {

    private final UserProfileRepository userRepo;
    private final UserPreferenceRepository prefRepo;
    private final ChatMessageRepository msgRepo;
    private final ConversationSummaryRepository sumRepo;

    public MemoryService(UserProfileRepository userRepo,
                         UserPreferenceRepository prefRepo,
                         ChatMessageRepository msgRepo,
                         ConversationSummaryRepository sumRepo) {
        this.userRepo = userRepo;
        this.prefRepo = prefRepo;
        this.msgRepo = msgRepo;
        this.sumRepo = sumRepo;
    }

    @Transactional
    public UserProfile loadOrCreateProfile(String userId) {
        return userRepo.findById(userId).orElseGet(() -> userRepo.save(new UserProfile(userId)));
    }

    @Transactional
    public void recordUserMessage(String userId, String text) {
        msgRepo.save(new ChatMessage(userId, "user", text));
        extractAndSaveFacts(userId, text);
    }

    @Transactional
    public void recordAssistantMessage(String userId, String text) {
        msgRepo.save(new ChatMessage(userId, "assistant", text));
    }

    public List<ChatMessage> fetchRecentMessages(String userId, int limit) {
        List<ChatMessage> last = msgRepo.findTop15ByUserIdOrderByCreatedAtDesc(userId);
        return last.stream().limit(limit).collect(Collectors.toList());
    }

    public List<UserPreference> getPreferences(String userId) {
        return prefRepo.findByUserId(userId);
    }

    public ConversationSummary getSummary(String userId) {
        return sumRepo.findById(userId).orElse(null);
    }

    @Transactional
    public void upsertSummary(String userId, String newSummary) {
        ConversationSummary s = sumRepo.findById(userId).orElse(new ConversationSummary(userId, ""));
        s.setSummary(newSummary);
        sumRepo.save(s);
    }

    /**
     * Toy NLP: pull simple facts like "my name is X", "I live in Y", "favorite color is Z".
     * This is just to demonstrate "personalized memory" for the assignment.
     */
    @Transactional
    public void extractAndSaveFacts(String userId, String text) {
        UserProfile p = loadOrCreateProfile(userId);
        String t = text.toLowerCase();

        // name
        int i = t.indexOf("my name is ");
        if (i >= 0) {
            String name = text.substring(i + 11).split("[.!?,\\n]")[0].trim();
            if (!name.isBlank()) { p.setName(name); userRepo.save(p); }
        }

        // location
        i = t.indexOf("i live in ");
        if (i >= 0) {
            String loc = text.substring(i + 10).split("[.!?,\\n]")[0].trim();
            if (!loc.isBlank()) { p.setLocation(loc); userRepo.save(p); }
        }

        // style
        if (t.contains("talk formally")) { p.setStyle("formal"); userRepo.save(p); }
        if (t.contains("talk casually") || t.contains("be casual")) { p.setStyle("casual"); userRepo.save(p); }

        // generic key-value: "my favorite X is Y"
        i = t.indexOf("my favorite ");
        if (i >= 0 && t.contains(" is ")) {
            String rest = text.substring(i + 12);
            String[] parts = rest.split(" is ", 2);
            if (parts.length == 2) {
                String key = parts[0].replaceAll("[^a-zA-Z0-9_ ]", "").trim().replace(" ", "_");
                String val = parts[1].split("[.!?,\\n]")[0].trim();
                if (!key.isBlank() && !val.isBlank()) upsertPref(userId, "favorite_" + key, val);
            }
        }
    }

    @Transactional
    public void upsertPref(String userId, String key, String value) {
        UserPreference existing = prefRepo.findFirstByUserIdAndKeyName(userId, key);
        if (existing == null) {
            prefRepo.save(new UserPreference(userId, key, value));
        } else {
            existing.setValueText(value);
            prefRepo.save(existing);
        }
    }
}
