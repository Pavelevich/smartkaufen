package com.ia.smartkaufen.controller;

import com.ia.smartkaufen.services.BatchProcessorService;
import com.ia.smartkaufen.services.OllamaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OllamaController {

    private final BatchProcessorService batchProcessorService;

    @GetMapping("/api/send-datato-ollama3")
    public String sendDataToOllama3(@RequestParam int userID, @RequestParam double budget,
                                    @RequestParam int days, @RequestParam int people) {
        batchProcessorService.processShoppingList(userID, budget, days, people);
        return "Processing shopping list...";
    }
}