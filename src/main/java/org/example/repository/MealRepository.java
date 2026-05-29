package org.example.repository;

import org.example.model.Diet;
import org.example.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MealRepository extends JpaRepository<Meal, UUID> {

    @Query("SELECT DISTINCT m FROM Meal m JOIN FETCH m.foods WHERE m.diet IN :diets")
    List<Meal> findByDietInFetchFoods(@Param("diets") List<Diet> diets);

}

