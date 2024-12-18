package fitness_app_be.fitness_app.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkoutPreference {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long userid;
    private FitnessGoal fitnessGoal;
    private FitnessLevel fitnessLevel;
    private TrainingStyle preferredTrainingStyle;
    private int daysAvailable;
}
