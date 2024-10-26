package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkoutMapper {

    private final UserMapper userMapper;

    public WorkoutMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Workout toDomain(WorkoutDTO workoutDTO) {
        return new Workout(workoutDTO.getId(), workoutDTO.getTrainerId(), workoutDTO.getName(),
                workoutDTO.getDescription(), workoutDTO.getPictureURL(),
                workoutDTO.getExercises(),
                workoutDTO.getUsers().stream().map(userMapper::toDomain).collect(Collectors.toList()));
    }

    public WorkoutDTO domainToDto(Workout workout) {
        return new WorkoutDTO(workout.getId(), workout.getTrainerId(), workout.getName(),
                workout.getDescription(), workout.getPictureURL(),
                workout.getExercises(),
                workout.getUsers().stream().map(userMapper::domainToDto).collect(Collectors.toList()));
    }
}
