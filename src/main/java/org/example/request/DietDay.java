package org.example.request;

import lombok.Data;

import java.util.List;

@Data
public class DietDay {
    private String day; // MONDAY, TUESDAY, ...
    private List<MealDto> meals;
}
