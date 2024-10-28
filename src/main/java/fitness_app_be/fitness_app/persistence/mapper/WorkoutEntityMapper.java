package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WorkoutEntityMapper {


    public Workout toDomain(WorkoutEntity workoutEntity) {
        if (workoutEntity == null) {
            return null;
        }
        return new Workout(
                workoutEntity.getId(),
                workoutEntity.getName(),
                workoutEntity.getDescription(),
                workoutEntity.getPictureURL(),
                workoutEntity.getExercises()
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
        workoutEntity.setExercises(workout.getExercises());



        return workoutEntity;
    }

}
