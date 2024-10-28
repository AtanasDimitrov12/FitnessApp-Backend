package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkoutMapper {

    private final ExerciseMapper exerciseMapper;

    @Autowired
    public WorkoutMapper(@Lazy ExerciseMapper exerciseMapper) {
        this.exerciseMapper = exerciseMapper;
    }

    public Workout toDomain(WorkoutDTO workoutDTO) {
        if (workoutDTO == null) {
            return null;
        }

        return new Workout(
                workoutDTO.getId(),
                workoutDTO.getName(),
                workoutDTO.getDescription(),
                workoutDTO.getPictureURL(),
                workoutDTO.getExercises() != null
                        ? workoutDTO.getExercises().stream()
                        .map(exerciseMapper::toDomain)
                        .collect(Collectors.toList())
                        : null
        );
    }

    public WorkoutDTO domainToDto(Workout workout) {
        if (workout == null) {
            return null;
        }

        return new WorkoutDTO(
                workout.getId(),
                workout.getName(),
                workout.getDescription(),
                workout.getPictureURL(),
                workout.getExercises() != null
                        ? workout.getExercises().stream()
                        .map(exerciseMapper::toDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
