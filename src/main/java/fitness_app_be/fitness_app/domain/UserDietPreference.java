package fitness_app_be.fitness_app.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDietPreference {
    @Setter(AccessLevel.NONE)
    private Long id;
    @Setter(AccessLevel.NONE)
    private Long userid;
    private int calories;
    private int mealFrequency;
}
