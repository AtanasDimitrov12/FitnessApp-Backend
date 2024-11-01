package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanDTO {
    private Long id;
    private List<UserDTO> users;
    private List<WorkoutDTO> workouts;
    private List<String> fitnessGoals;
    private List<String> trainingStyles;
}
