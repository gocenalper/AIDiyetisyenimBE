package org.example.request;

import lombok.Data;

import java.util.List;

@Data
public class MealDto {
    private String mealType; // BREAKFAST, LUNCH, etc.
    private List<FoodDto> foods;
}
