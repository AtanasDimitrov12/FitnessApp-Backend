package fitness_app_be.fitness_app.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDietPreference {
    @Setter(AccessLevel.NONE)
    private Long id;
    private User user;
    private int calories;
    private int mealFrequency;
}
