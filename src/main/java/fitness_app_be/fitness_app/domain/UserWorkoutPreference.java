package fitness_app_be.fitness_app.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkoutPreference {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long userid;
    private String fitnessGoal;
    private String fitnessLevel;
    private String preferredTrainingStyle;
    private int daysAvailable;
}
