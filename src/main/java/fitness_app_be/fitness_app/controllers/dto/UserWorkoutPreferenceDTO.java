package fitness_app_be.fitness_app.controllers.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class    UserWorkoutPreferenceDTO {
    private Long id;
    private Long userid;
    private String fitnessGoal;
    private String fitnessLevel;
    private String preferredTrainingStyle;
    private int daysAvailable;
}
