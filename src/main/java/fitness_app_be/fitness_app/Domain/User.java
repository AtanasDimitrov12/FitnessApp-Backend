package fitness_app_be.fitness_app.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String fitnessGoal;
    private String dietPreference;
}
