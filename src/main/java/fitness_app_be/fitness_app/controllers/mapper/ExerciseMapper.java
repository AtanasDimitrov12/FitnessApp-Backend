package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ExerciseMapper {

    private final WorkoutMapper workoutMapper;

    @Autowired
    public ExerciseMapper(@Lazy WorkoutMapper workoutMapper) {
        this.workoutMapper = workoutMapper;
    }

    public Exercise toDomain(ExerciseDTO exerciseDTO) {
        if (exerciseDTO == null) {
            return null;
        }

        return new Exercise(
                exerciseDTO.getId(),
                exerciseDTO.getName(),
                exerciseDTO.getSets(),
                exerciseDTO.getReps(),
                exerciseDTO.getWorkouts() != null
                        ? exerciseDTO.getWorkouts().stream()
                        .map(workoutMapper::toDomain)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }

    public ExerciseDTO toDto(Exercise exercise) {
        if (exercise == null) {
            return null;
        }

        return new ExerciseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getWorkouts() != null
                        ? exercise.getWorkouts().stream()
                        .map(workoutMapper::domainToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}
