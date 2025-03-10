package com.yatish.ollama.deepseek;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ChatController {

    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        String rawResponse = this.chatModel.call(message);
        String cleanResponse = rawResponse.replaceAll("<think>|</think>", "").trim(); // Remove unwanted tags
        return Map.of("generation", cleanResponse);
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

    // New Endpoint for Recipe Chatbot
    @GetMapping("/ai/recipe")
    public Map<String, String> getRecipe(@RequestParam(value = "recipe", required = true) String recipe) {
        String query = "Provide a detailed recipe for " + recipe + ". Include ingredients and step-by-step cooking instructions.";
        String response = this.chatModel.call(query);
        return Map.of("recipe", response);
    }
}
