package com.chatbot.controller;

import com.chatbot.model.dto.ChatRequest;
import com.chatbot.model.dto.ChatResponse;
import com.chatbot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    private final ChatService chat;

    public ChatController(ChatService chat) {
        this.chat = chat;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) throws Exception {
        String reply = chat.reply(req.getUserId(), req.getMessage());
        // tone is currently derived inside service; expose "casual" as default since demo UI doesn't request it
        return ResponseEntity.ok(new ChatResponse(reply, "auto"));
    }
}
