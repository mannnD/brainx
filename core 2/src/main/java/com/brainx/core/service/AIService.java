package com.brainx.core.service;

import com.brainx.core.domain.AiRequest;
import org.springframework.stereotype.Service;

@Service
public class AIService {
//    private final TensorFlow tensorflow;

    public String summarizeText(AiRequest aiRequest) {
        return "summary";
    }
}
