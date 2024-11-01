package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class WorkoutEntityMapper {

    private ExerciseEntityMapper exerciseEntityMapper;
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;

    @Autowired
    public WorkoutEntityMapper(@Lazy ExerciseEntityMapper exerciseEntityMapper, @Lazy WorkoutPlanEntityMapper workoutPlanEntityMapper) {
        this.exerciseEntityMapper = exerciseEntityMapper;
        this.workoutPlanEntityMapper = workoutPlanEntityMapper;
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
                        .toList()
                        : null,
                workoutEntity.getWorkoutPlans() != null
                        ? workoutEntity.getWorkoutPlans().stream()
                        .map(workoutPlanEntityMapper::toDomain)
                        .toList()
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
                    .toList());
        }

        if (workout.getWorkoutPlans() != null) {
            workoutEntity.setWorkoutPlans(workout.getWorkoutPlans().stream()
                    .map(workoutPlanEntityMapper::toEntity)
                    .toList());
        }

        return workoutEntity;
    }
}
