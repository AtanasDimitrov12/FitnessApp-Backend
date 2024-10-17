package fitness_app_be.fitness_app.persistence.Mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.Entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.Entity.WorkoutEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkoutEntityMapper {

    public Workout entityToDomain(WorkoutEntity workoutEntity) {
        if (workoutEntity == null) {
            return null;
        }

        return new Workout(
                workoutEntity.getId(),
                workoutEntity.getTrainer().getId(),
                workoutEntity.getName(),
                workoutEntity.getDescription(),
                workoutEntity.getPictureURL(),
                workoutEntity.getExercises()
        );
    }

    public WorkoutEntity domainToEntity(Workout workout) {
        if (workout == null) {
            return null;
        }
        WorkoutEntity workoutEntity = new WorkoutEntity();
        workoutEntity.setId(workout.getId());
        workoutEntity.setName(workout.getName());
        workoutEntity.setDescription(workout.getDescription());
        workoutEntity.setPictureURL(workout.getPictureURL());
        workoutEntity.setExercises(workout.getExercises());

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId(workout.getTrainerId());
        workoutEntity.setTrainer(trainerEntity);

        return workoutEntity;
    }
}
