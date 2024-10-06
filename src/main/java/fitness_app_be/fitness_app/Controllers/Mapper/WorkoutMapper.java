package fitness_app_be.fitness_app.Controllers.Mapper;

import fitness_app_be.fitness_app.Controllers.DTOs.WorkoutDTO;
import fitness_app_be.fitness_app.Controllers.DTOs.WorkoutDTO;
import fitness_app_be.fitness_app.Domain.Workout;
import fitness_app_be.fitness_app.Domain.Workout;
import org.springframework.stereotype.Component;

@Component
public class WorkoutMapper {



    public Workout toDomain(WorkoutDTO workoutDTO) {
        return new Workout(workoutDTO.getId(), workoutDTO.getName(), workoutDTO.getDescription(), workoutDTO.getPictureURL(),
                workoutDTO.getExercises());
    }


    public WorkoutDTO domainToDto(Workout workout) {
        return new WorkoutDTO(workout.getId(), workout.getName(), workout.getDescription(), workout.getPictureURL(),
                workout.getExercises());
    }

}
