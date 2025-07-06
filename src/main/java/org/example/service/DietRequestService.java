package org.example.service;

import org.example.model.DietRequest;

public interface DietRequestService {

    void createAndQueueRequest(DietRequest request);
}
