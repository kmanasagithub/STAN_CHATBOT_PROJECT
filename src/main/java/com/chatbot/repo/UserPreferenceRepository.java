package com.chatbot.repo;

import com.chatbot.model.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    List<UserPreference> findByUserId(String userId);
    UserPreference findFirstByUserIdAndKeyName(String userId, String keyName);
}
