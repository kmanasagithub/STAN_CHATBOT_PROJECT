package com.chatbot.repo;

import com.chatbot.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop15ByUserIdOrderByCreatedAtDesc(String userId);
}
