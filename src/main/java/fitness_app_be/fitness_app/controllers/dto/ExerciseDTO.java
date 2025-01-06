package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.MuscleGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String name;
    private int sets;
    private int reps;
    private MuscleGroup muscleGroup;
}

