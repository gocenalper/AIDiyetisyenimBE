package org.example.repository;

import org.example.model.Diet;
import org.example.model.DietRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
}

