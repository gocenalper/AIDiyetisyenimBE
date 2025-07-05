package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.enums.DietRequestState;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "diet_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "target_weight", nullable = false)
    private Double targetWeight;

    @Column(name = "activity_level", nullable = false, length = 50)
    private String activityLevel;

    @ElementCollection
    @CollectionTable(name = "diet_liked_foods", joinColumns = @JoinColumn(name = "diet_request_id"))
    @Column(name = "liked_food")
    private List<String> likedFoods;

    @ElementCollection
    @CollectionTable(name = "diet_disliked_foods", joinColumns = @JoinColumn(name = "diet_request_id"))
    @Column(name = "disliked_food")
    private List<String> dislikedFoods;

    @ElementCollection
    @CollectionTable(name = "diet_allergic_foods", joinColumns = @JoinColumn(name = "diet_request_id"))
    @Column(name = "allergic_food")
    private List<String> allergicFoods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 30)
    private DietRequestState requestState;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}