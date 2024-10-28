package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkoutMapper {


    public Workout toDomain(WorkoutDTO workoutDTO) {
        return new Workout(workoutDTO.getId(), workoutDTO.getName(),
                workoutDTO.getDescription(), workoutDTO.getPictureURL(),
                workoutDTO.getExercises());
    }

    public WorkoutDTO domainToDto(Workout workout) {
        return new WorkoutDTO(workout.getId(), workout.getName(),
                workout.getDescription(), workout.getPictureURL(),
                workout.getExercises());
    }
}
