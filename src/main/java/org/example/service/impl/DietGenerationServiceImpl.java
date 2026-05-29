package org.example.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.*;
import org.example.model.enums.MealType;
import org.example.model.enums.Unit;
import org.example.repository.DietRepository;
import org.example.repository.FoodRepository;
import org.example.repository.MealRepository;
import org.example.repository.UserRepository;
import org.example.service.DietGenerationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class DietGenerationServiceImpl implements DietGenerationService {

    private final ObjectMapper objectMapper;
    private final DietRepository dietRepository;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @Override
    public void createDietFromJson(String jsonResponse, User user) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            log.info("Received diet JSON: {}", jsonResponse);

            LocalDate startDate = LocalDate.now();

            for (int i = 0; i < root.size(); i++) {
                JsonNode dayNode = root.get(i);
                String dayText = dayNode.get("day").asText().toUpperCase(Locale.ENGLISH); // örn: "MONDAY"
                LocalDate currentDate = startDate.plusDays(i);

                // Diet kaydı oluştur
                Diet diet = Diet.builder()
                        .user(user)
                        .dietStartDate(currentDate)
                        .dietEndDate(currentDate)
                        .build();
                dietRepository.save(diet);

                JsonNode mealsNode = dayNode.get("meals");

                for (JsonNode mealNode : mealsNode) {
                    MealType mealType = MealType.valueOf(mealNode.get("mealType").asText().toUpperCase(Locale.ENGLISH));
                    List<Food> foodSet = new ArrayList<>();

                    for (JsonNode foodNode : mealNode.get("foods")) {
                        Food food = Food.builder()
                                .name(foodNode.get("name").asText())
                                .amount(new BigDecimal(foodNode.get("amount").asText()))
                                .unit(Unit.valueOf(foodNode.get("unit").asText().toUpperCase(Locale.ENGLISH)))
                                .calories(new BigDecimal(foodNode.get("calories").asText()))
                                .build();
                        foodRepository.save(food);
                        foodSet.add(food);
                    }

                    Meal meal = Meal.builder()
                            .diet(diet)
                            .mealType(mealType)
                            .dayOfWeek(DayOfWeek.valueOf(dayText))
                            .foods(foodSet)
                            .build();
                    mealRepository.save(meal);
                }

                log.info("Created diet for user {} on {}", user.getEmail(), currentDate);
            }

        } catch (Exception e) {
            log.error("Error while parsing diet JSON and saving to DB", e);
        }
    }
}
