package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "diet")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diet {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "diet", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Meal meal;

    @Column(nullable = false)
    private LocalDate dietStartDate;

    @Column(nullable = false)
    private LocalDate dietEndDate;

}
