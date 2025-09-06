package com.chatbot.repo;


import com.chatbot.model.entity.ConversationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationSummaryRepository extends JpaRepository<ConversationSummary, String> {}

