package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDTO {

    private Long id;
    private String name;
    private String description;
    private String pictureURL;
    private List<ExerciseDTO> exercises = new ArrayList<>();
    private List<WorkoutPlanDTO> workoutPlans = new ArrayList<>();
}


