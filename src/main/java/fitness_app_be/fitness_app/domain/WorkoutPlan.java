package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlan {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long userId;
    private List<Workout> workouts;
}
