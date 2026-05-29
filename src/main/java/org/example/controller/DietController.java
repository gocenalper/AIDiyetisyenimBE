package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.DietRequest;
import org.example.request.GetCurrentDietRequest;
import org.example.request.GetCurrentDietResponse;
import org.example.service.DietService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @PostMapping("/request")
    public ResponseEntity<String> createDietRequest(@RequestBody final DietRequest request) {
        dietService.createAndQueueRequest(request);

        return new ResponseEntity<>(
                "Diyet isteğiniz alındı. Yakında bilgilendirileceksiniz.",
                HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/current")
    public ResponseEntity<GetCurrentDietResponse> getCurrentDiet(@RequestBody final GetCurrentDietRequest request) {
        final GetCurrentDietResponse response =  dietService.getDietResponseForUser(request);

        return ResponseEntity.ok(response);
    }
}
