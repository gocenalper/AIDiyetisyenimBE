package org.example.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.Unit;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Table(name = "food")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private BigDecimal calories;

    @ManyToMany(mappedBy = "foods")
    private Set<Meal> meals;
}
