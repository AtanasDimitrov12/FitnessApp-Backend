package fitness_app_be.fitness_app.controllers.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDietPreferenceDTO {
    private Long id;
    private Long userId;
    private int calories;
    private int mealFrequency;
}
