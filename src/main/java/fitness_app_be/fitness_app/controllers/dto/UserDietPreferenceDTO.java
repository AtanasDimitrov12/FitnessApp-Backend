package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDietPreferenceDTO {
    private Long id;
    private User user;
    private int calories;
    private int mealFrequency;
}
