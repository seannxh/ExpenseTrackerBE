package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.ChatAiModel;
import com.springboot.api.expensetracker.service.ChatAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatAIController {

    private final ChatAiService chatAIService;

    public ChatAIController(ChatAiService chatAIService) {
        this.chatAIService = chatAIService;
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatAiModel request, Authentication authentication) {
        String userEmail = authentication.getName();
        String reply = chatAIService.getAIResponse(request.getMessage(), userEmail);
        return ResponseEntity.ok(Map.of("response", reply));
    }
}
