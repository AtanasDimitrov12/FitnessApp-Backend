package fitness_app_be.fitness_app.persistence.entity;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "diet_meal",
            joinColumns = @JoinColumn(name = "diet_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<MealEntity> meals = new ArrayList<>();


    public void addMeal(MealEntity meal) {
        if (meals == null) {
            meals = new ArrayList<>(); // Ensure mutability
        }
        if (!meals.contains(meal)) {
            meals.add(meal);
            meal.getDiets().add(this); // Maintain bidirectional sync
        }
    }


    public void removeMeal(MealEntity meal) {
        if (meals != null && meals.contains(meal)) {
            meals.remove(meal);
            meal.removeDiet(this); // Maintain bidirectional relationship
        }
    }

}
