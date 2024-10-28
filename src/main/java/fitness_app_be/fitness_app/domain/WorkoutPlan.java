package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlan {
    @Setter(AccessLevel.NONE)
    private Long id;
    @Setter(AccessLevel.NONE)
    private Long userId;
    private List<Workout> workouts = new ArrayList<>();
    private List<String> fitnessGoals = new ArrayList<>();
    private List<String> trainingStyle = new ArrayList<>();
}
