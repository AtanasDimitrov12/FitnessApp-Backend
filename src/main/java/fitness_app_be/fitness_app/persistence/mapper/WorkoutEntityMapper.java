package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class WorkoutEntityMapper {

    private ExerciseEntityMapper exerciseEntityMapper;

    @Autowired
    public WorkoutEntityMapper(@Lazy ExerciseEntityMapper exerciseEntityMapper) {
        this.exerciseEntityMapper = exerciseEntityMapper;
    }

    public Workout toDomain(WorkoutEntity workoutEntity) {
        if (workoutEntity == null) {
            return null;
        }
        return new Workout(
                workoutEntity.getId(),
                workoutEntity.getName(),
                workoutEntity.getDescription(),
                workoutEntity.getPictureURL(),
                workoutEntity.getExercises() != null
                        ? workoutEntity.getExercises().stream()
                        .map(exerciseEntityMapper::toDomain)
                        .collect(Collectors.toList())
                        : null
        );
    }

    public WorkoutEntity toEntity(Workout workout) {
        if (workout == null) {
            return null;
        }

        WorkoutEntity workoutEntity = new WorkoutEntity();
        workoutEntity.setId(workout.getId());
        workoutEntity.setName(workout.getName());
        workoutEntity.setDescription(workout.getDescription());
        workoutEntity.setPictureURL(workout.getPictureURL());

        if (workout.getExercises() != null) {
            workoutEntity.setExercises(workout.getExercises().stream()
                    .map(exerciseEntityMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return workoutEntity;
    }
}
