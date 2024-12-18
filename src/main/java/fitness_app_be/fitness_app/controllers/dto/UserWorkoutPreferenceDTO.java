package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.FitnessGoal;
import fitness_app_be.fitness_app.domain.FitnessLevel;
import fitness_app_be.fitness_app.domain.TrainingStyle;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class    UserWorkoutPreferenceDTO {
    private Long id;
    private Long userid;
    private FitnessGoal fitnessGoal;
    private FitnessLevel fitnessLevel;
    private TrainingStyle preferredTrainingStyle;
    private int daysAvailable;
}
