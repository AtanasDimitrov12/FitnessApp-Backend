package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
                        .toList()
                        : Collections.emptyList(),
                workoutDTO.getFitnessGoals() != null
                        ? List.copyOf(workoutDTO.getFitnessGoals())
                        : Collections.emptyList(),
                workoutDTO.getFitnessLevels() != null
                        ? List.copyOf(workoutDTO.getFitnessLevels())
                        : Collections.emptyList(),
                workoutDTO.getTrainingStyles() != null
                        ? List.copyOf(workoutDTO.getTrainingStyles())
                        : Collections.emptyList()
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
                        .toList()
                        : Collections.emptyList(),
                workout.getFitnessGoals() != null
                        ? List.copyOf(workout.getFitnessGoals())
                        : Collections.emptyList(),
                workout.getFitnessLevels() != null
                        ? List.copyOf(workout.getFitnessLevels())
                        : Collections.emptyList(),
                workout.getTrainingStyles() != null
                        ? List.copyOf(workout.getTrainingStyles())
                        : Collections.emptyList()
        );
    }
}
