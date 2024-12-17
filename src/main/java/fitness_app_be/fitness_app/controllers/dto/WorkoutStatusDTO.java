package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutStatusDTO {
    private Long id;
    private WorkoutPlanDTO workoutPlan;
    private WorkoutDTO workout;
    private Boolean isDone;
    private Integer weekNumber;
}
