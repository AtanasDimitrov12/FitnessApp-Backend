package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Workout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanDTO {
    private Long id;
    private List<UserDTO> users = new ArrayList<>();
    private List<WorkoutDTO> workouts = new ArrayList<>();
    private List<String> fitnessGoals = new ArrayList<>();
    private List<String> trainingStyles = new ArrayList<>();
}
