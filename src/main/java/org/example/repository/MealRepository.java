package org.example.repository;

import org.example.model.Diet;
import org.example.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MealRepository extends JpaRepository<Meal, UUID> {
}

