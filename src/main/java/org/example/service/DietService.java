package org.example.service;

import org.example.model.DietRequest;
import org.example.model.User;
import org.example.request.GetCurrentDietRequest;
import org.example.request.GetCurrentDietResponse;

public interface DietService {

    void createAndQueueRequest(DietRequest request);

    GetCurrentDietResponse getDietResponseForUser(GetCurrentDietRequest user);
}
