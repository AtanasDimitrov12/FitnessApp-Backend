package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO {
    private Long id;
    private Long trainerId;
    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private double cookingTime;
}
