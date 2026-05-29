package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Diet;
import org.example.model.DietRequest;
import org.example.model.Meal;
import org.example.model.User;
import org.example.model.enums.DietRequestState;
import org.example.repository.DietRepository;
import org.example.repository.DietRequestRepository;
import org.example.repository.MealRepository;
import org.example.repository.UserRepository;
import org.example.request.*;
import org.example.service.DietService;
import org.example.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietRequestRepository dietRequestRepository;
    private final MealRepository mealRepository;
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

    @Override
    public GetCurrentDietResponse getDietResponseForUser(final GetCurrentDietRequest request) {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails userDetails) {
            user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if (Objects.isNull(user)) {
                log.error("User cannot be found.. terminating..");

                throw new RuntimeException("User could not be found");
            }

        }

        List<Diet> diets = dietRepository.findByUser(user);

        final Map<DayOfWeek, List<Meal>> mealsGrouped = mealRepository.findByDietInFetchFoods(diets).stream()
                .collect(Collectors.groupingBy(Meal::getDayOfWeek));

        final List<DietDay> days = mealsGrouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Pazartesiden başlasın
                .map(entry -> {
                    DietDay dayDto = new DietDay();
                    dayDto.setDay(entry.getKey().name());

                    final List<MealDto> mealDtos = entry.getValue().stream().map(meal -> {
                        MealDto mealDto = new MealDto();
                        mealDto.setMealType(meal.getMealType().name());

                        final List<FoodDto> foodDtos = meal.getFoods().stream().map(food -> {
                            FoodDto foodDto = new FoodDto();
                            foodDto.setName(food.getName());
                            foodDto.setAmount(food.getAmount());
                            foodDto.setUnit(food.getUnit().name());
                            foodDto.setCalories(food.getCalories());
                            return foodDto;
                        }).toList();

                        mealDto.setFoods(foodDtos);
                        return mealDto;
                    }).toList();

                    dayDto.setMeals(mealDtos);
                    return dayDto;
                }).toList();

        final GetCurrentDietResponse response = new GetCurrentDietResponse();

        response.setDays(days);

        return response;
    }
}
