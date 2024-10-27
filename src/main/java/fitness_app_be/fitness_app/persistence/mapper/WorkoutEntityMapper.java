package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaTrainerRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class WorkoutEntityMapper {

    private UserEntityMapper userEntityMapperImpl;
    private TrainerEntityMapper trainerEntityMapperImpl;
    private JpaTrainerRepository jpaTrainerRepository;

    @Autowired
    public WorkoutEntityMapper(@Lazy UserEntityMapper userEntityMapperImpl, @Lazy TrainerEntityMapper trainerEntityMapperImpl, JpaTrainerRepository jpaTrainerRepository) {
        this.userEntityMapperImpl = userEntityMapperImpl;
        this.trainerEntityMapperImpl = trainerEntityMapperImpl;
        this.jpaTrainerRepository = jpaTrainerRepository;
    }

    public Workout toDomain(WorkoutEntity workoutEntity) {
        if (workoutEntity == null) {
            return null;
        }
        return new Workout(
                workoutEntity.getId(),
                workoutEntity.getTrainer().getId(),
                workoutEntity.getName(),
                workoutEntity.getDescription(),
                workoutEntity.getPictureURL(),
                workoutEntity.getExercises(),
                workoutEntity.getUsers().stream()
                        .map(userEntityMapperImpl::toDomain)
                        .collect(Collectors.toList())
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

        // Initialize users list if null to prevent NullPointerException
        if (workout.getUsers() != null) {
            workoutEntity.setUsers(workout.getUsers().stream()
                    .map(userEntityMapperImpl::toEntity)
                    .collect(Collectors.toList()));
        } else {
            workoutEntity.setUsers(new ArrayList<>()); // Initialize to an empty list if null
        }

        if (workout.getTrainerId() != null) {
            TrainerEntity trainerEntity = jpaTrainerRepository.findById(workout.getTrainerId())
                    .orElseThrow(() -> new IllegalArgumentException("Trainer not found with ID: " + workout.getTrainerId()));
            workoutEntity.setTrainer(trainerEntity);
        } else {
            throw new IllegalArgumentException("Trainer ID must not be null when creating a WorkoutEntity");
        }

        return workoutEntity;
    }

}
