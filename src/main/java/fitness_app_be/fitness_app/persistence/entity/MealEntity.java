package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private int calories;
    private int protein;
    private int carbs;

    @Column(name = "cooking_time")
    private double cookingTime;

    @ManyToMany(mappedBy = "meals", fetch = FetchType.LAZY)
    private List<DietEntity> diets = new ArrayList<>(); // Always initialize to mutable

    public void addDiet(DietEntity diet) {
        if (diets == null) {
            diets = new ArrayList<>();
        }
        if (!diets.contains(diet)) {
            diets.add(diet);
            diet.addMeal(this); // Maintain bidirectional relationship
        }
    }

    public void removeDiet(DietEntity diet) {
        if (diets != null && diets.contains(diet)) {
            diets.remove(diet);
            diet.removeMeal(this); // Maintain bidirectional relationship
        }
    }

}

