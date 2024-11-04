package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private int sets;
    private int reps;
    private String muscleGroup;
    private List<Workout> workouts;
}
