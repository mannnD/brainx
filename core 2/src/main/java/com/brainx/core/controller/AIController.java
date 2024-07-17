package com.brainx.core.controller;

import com.brainx.core.domain.AiRequest;
import com.brainx.core.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ai")
public class AIController {
    private final AIService aiService;
    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/summarize")
    public String getSummary(AiRequest aiRequest){
        return aiService.summarizeText(aiRequest);
    }
}
