package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ExerciseEntityMapper {


    public Exercise toDomain(ExerciseEntity exerciseEntity) {
        if (exerciseEntity == null) {
            return null;
        }

        return new Exercise(
                exerciseEntity.getId(),
                exerciseEntity.getName(),
                exerciseEntity.getSets(),
                exerciseEntity.getReps(),
                exerciseEntity.getMuscleGroup()
        );
    }

    public ExerciseEntity toEntity(Exercise exercise) {
        if (exercise == null) {
            return null;
        }

        return ExerciseEntity.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .muscleGroup(exercise.getMuscleGroup())
                .build();


    }
}
