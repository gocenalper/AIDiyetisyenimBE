package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.DietRequest;
import org.example.model.User;
import org.example.model.enums.DietRequestState;
import org.example.repository.DietRequestRepository;
import org.example.repository.UserRepository;
import org.example.service.DietRequestService;
import org.example.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietRequestServiceImpl implements DietRequestService {

    private final DietRequestRepository dietRequestRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void createAndQueueRequest(final DietRequest request) {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            final User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if (Objects.isNull(user)) {
                log.error("User cannot be found.. terminating..");
            }

            request.setUser(user);
            request.setRequestState(DietRequestState.PENDING);
        }

        final DietRequest savedRequest = dietRequestRepository.save(request);

        try {
            String rawJson = objectMapper.writeValueAsString(request);

            kafkaTemplate.send(Constants.DIET_REQUEST_TOPIC, rawJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize DietRequest to JSON", e);
        }
    }
}
