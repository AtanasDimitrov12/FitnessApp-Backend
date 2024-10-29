package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietDTO {
    private Long id;
    private String name;
    private String description;
    private String pictureURL;
    private List<UserDTO> users = new ArrayList<>();
    private List<MealDTO> meals = new ArrayList<>();
}
