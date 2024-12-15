package fitness_app_be.fitness_app.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "diet_meal",
            joinColumns = @JoinColumn(name = "diet_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    @JsonManagedReference
    private List<MealEntity> meals = new ArrayList<>();

    public void addMeal(MealEntity meal) {
        if (meals == null) {
            meals = new ArrayList<>();
        }
        if (!meals.contains(meal)) {
            meals.add(meal);
            meal.getDiets().add(this); // Maintain bidirectional relationship
        }
    }

    public void removeMeal(MealEntity meal) {
        if (meals != null && meals.contains(meal)) {
            meals.remove(meal);
            meal.getDiets().remove(this); // Maintain bidirectional relationship
        }
    }
}
