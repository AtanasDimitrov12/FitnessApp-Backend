package fitness_app_be.fitness_app.controllers.dto.websockets;

import lombok.Data;

@Data
public class WorkoutDonePayload {
    private Long workoutPlanId;
    private Long workoutId;
    private Long userId;
}
