package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietDTO {
    private Long id;
    private Long trainerId;
    private String name;
    private String description;
    private String pictureURL;
    private List<MealDTO> meals;
    private List<UserDTO> users;
}
