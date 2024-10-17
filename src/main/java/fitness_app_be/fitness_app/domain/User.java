package fitness_app_be.fitness_app.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String username;
    private String email;
    private String fitnessGoal;
    private String dietPreference;
    private String pictureURL;
}
