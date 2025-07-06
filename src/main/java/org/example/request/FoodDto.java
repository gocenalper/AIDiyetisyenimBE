package org.example.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodDto {
    private String name;
    private BigDecimal amount;
    private String unit;
    private BigDecimal calories;
}