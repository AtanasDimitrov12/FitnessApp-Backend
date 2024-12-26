package fitness_app_be.fitness_app.controllers.dto.webSockets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutUpdateMessage {
    private Long workoutId;
    private String workoutName;
}
