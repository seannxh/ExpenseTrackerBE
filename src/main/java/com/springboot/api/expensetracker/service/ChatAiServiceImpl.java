package com.springboot.api.expensetracker.service;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.ExpenseRepository;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatAiServiceImpl implements ChatAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ChatAiServiceImpl(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public String getAIResponse(String userMessage, String userEmail) {
        UserModel user = userRepository.findByEmail(userEmail).orElseThrow();
        List<ExpenseModel> expenses = expenseRepository.findByUser(user);

        String expenseSummary = expenses.stream()
                .map(e -> e.getTitle() + ": $" + e.getAmount())
                .collect(Collectors.joining(", "));

        String fullPrompt = "You are a helpful budgeting assistant. Based on the following user expenses, respond to the question: "
                + userMessage
                + ".\n\nExpenses: " + expenseSummary;


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(Map.of("role", "user", "content", fullPrompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString();
        }catch(HttpClientErrorException.TooManyRequests e){
            return "You've exceeded your OpenAI quota. Please check your usage or billing on the OpenAI dashboard.";
        }catch(Exception e){
            return "An unexpected error occurred while contacting the AI service. Please try again later.";
        }
    }

}
