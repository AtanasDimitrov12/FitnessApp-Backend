package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "meal")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "calories")
    private int calories;

    @Column(name = "protein")
    private int protein;

    @Column(name = "carbs")
    private int carbs;

    @Column(name = "cooking_time")
    private double cookingTime;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietEntity diet;
}
