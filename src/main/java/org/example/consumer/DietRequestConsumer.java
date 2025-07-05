package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.DietRequest;
import org.example.openai.OpenAiClient;
import org.example.openai.PromptBuilder;
import org.example.service.DietGenerationService;
import org.example.util.Constants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietRequestConsumer {

    private final ObjectMapper objectMapper;
    private final OpenAiClient openAiClient;
    private final DietGenerationService dietGenerationService;

    @KafkaListener(topics = Constants.DIET_REQUEST_TOPIC, groupId = "diet-request-group")
    public void consumeDietRequest(String message) {
        try {
            final DietRequest request = objectMapper.readValue(message, DietRequest.class);
            String prompt = PromptBuilder.buildPrompt(request);
            String jsonResponse = openAiClient.callOpenAi(prompt);
            dietGenerationService.createDietFromJson(jsonResponse, request.getUser());
        } catch (Exception e) {
            log.error("Failed to process diet request: {}", e.getMessage(), e);
        }
    }
}
