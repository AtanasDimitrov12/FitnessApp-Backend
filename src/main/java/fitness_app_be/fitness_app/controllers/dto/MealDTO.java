package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO {
    private Long id;
    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private double cookingTime;
    private List<DietDTO> diets = new ArrayList<>();
}
