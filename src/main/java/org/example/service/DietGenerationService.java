package org.example.service;

import org.example.model.User;

public interface DietGenerationService {

    void createDietFromJson(String jsonResponse, User user);
}
