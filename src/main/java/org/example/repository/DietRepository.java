package org.example.repository;

import org.example.model.Diet;
import org.example.model.DietRequest;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {

    List<Diet> findByUser(User user);
}

