package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.domain.Exercise;
import org.springframework.stereotype.Component;


@Component
public class ExerciseMapper {

    public Exercise toDomain(ExerciseDTO exerciseDTO) {
        if (exerciseDTO == null) {
            return null;
        }

        return new Exercise(
                exerciseDTO.getId(),
                exerciseDTO.getName(),
                exerciseDTO.getSets(),
                exerciseDTO.getReps(),
                exerciseDTO.getMuscleGroup()
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
                exercise.getMuscleGroup()
        );
    }
}
