package fitness_app_be.fitness_app.domain;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutStatus {
    @Setter(AccessLevel.NONE)
    private Long id;
    private WorkoutPlan workoutPlan;
    private Workout workout;
    private Boolean isDone;
    private Integer weekNumber;
}
