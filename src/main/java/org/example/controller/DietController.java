package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.DietRequest;
import org.example.service.DietRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietRequestService dietRequestService;

    @PostMapping("/request")
    public ResponseEntity<String> createDietRequest(@RequestBody final DietRequest request) {
        dietRequestService.createAndQueueRequest(request);

        return new ResponseEntity<>(
                "Diyet isteğiniz alındı. Yakında bilgilendirileceksiniz.",
                HttpStatus.ACCEPTED);
    }
}
