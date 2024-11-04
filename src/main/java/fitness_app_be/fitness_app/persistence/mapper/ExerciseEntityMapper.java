package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ExerciseEntityMapper {

    private WorkoutEntityMapper workoutEntityMapper;

    @Autowired
    public ExerciseEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
    }

    public Exercise toDomain(ExerciseEntity exerciseEntity) {
        if (exerciseEntity == null) {
            return null;
        }

        return new Exercise(
                exerciseEntity.getId(),
                exerciseEntity.getName(),
                exerciseEntity.getSets(),
                exerciseEntity.getReps(),
                exerciseEntity.getMuscleGroup(),
                exerciseEntity.getWorkouts() != null
                        ? exerciseEntity.getWorkouts().stream()
                        .map(workoutEntityMapper::toDomain)
                        .toList()
                        : null
        );
    }

    public ExerciseEntity toEntity(Exercise exercise) {
        if (exercise == null) {
            return null;
        }

        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .muscleGroup(exercise.getMuscleGroup())
                .build();

        if (exercise.getWorkouts() != null) {
            exerciseEntity.setWorkouts(
                    exercise.getWorkouts().stream()
                            .map(workoutEntityMapper::toEntity)
                            .toList()
            );
        }

        return exerciseEntity;
    }
}
