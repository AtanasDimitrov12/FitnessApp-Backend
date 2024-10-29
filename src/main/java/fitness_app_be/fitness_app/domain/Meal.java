package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private double cookingTime;
    private List<Diet> diets = new ArrayList<>();
}
