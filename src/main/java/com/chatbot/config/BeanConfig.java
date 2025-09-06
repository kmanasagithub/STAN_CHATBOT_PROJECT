package com.chatbot.config;

import com.chatbot.llm.GeminiClient;
import com.chatbot.llm.LlmClient;
import com.chatbot.service.ToneService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean public ToneService toneService() { return new ToneService(); }
    
    @Bean 
    public LlmClient llmClient(@Value("${gemini.api.key}") String apiKey) {
        return new GeminiClient(apiKey);
    }
}

