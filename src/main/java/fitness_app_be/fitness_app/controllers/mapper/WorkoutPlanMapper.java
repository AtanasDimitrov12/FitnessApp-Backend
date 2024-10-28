package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class WorkoutPlanMapper {

    private final WorkoutMapper workoutMapper;

    public WorkoutPlanMapper(WorkoutMapper workoutMapper) {
        this.workoutMapper = workoutMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanDTO workoutPlanDTO) {
        if (workoutPlanDTO == null) {
            return null;
        }

        return new WorkoutPlan(
                workoutPlanDTO.getId(),
                workoutPlanDTO.getUserId(),
                workoutPlanDTO.getWorkouts() != null
                        ? workoutPlanDTO.getWorkouts().stream()
                        .map(workoutMapper::toDomain)
                        .collect(Collectors.toList())
                        : null,
                workoutPlanDTO.getFitnessGoals() != null
                        ? new ArrayList<>(workoutPlanDTO.getFitnessGoals())
                        : null,
                workoutPlanDTO.getTrainingStyle() != null
                        ? new ArrayList<>(workoutPlanDTO.getTrainingStyle())
                        : null
        );
    }

    public WorkoutPlanDTO domainToDto(WorkoutPlan workoutPlan) {
        if (workoutPlan == null) {
            return null;
        }

        return new WorkoutPlanDTO(
                workoutPlan.getId(),
                workoutPlan.getUserId(),
                workoutPlan.getWorkouts() != null
                        ? workoutPlan.getWorkouts().stream()
                        .map(workoutMapper::domainToDto)
                        .collect(Collectors.toList())
                        : null,
                workoutPlan.getFitnessGoals() != null
                        ? new ArrayList<>(workoutPlan.getFitnessGoals())
                        : null,
                workoutPlan.getTrainingStyle() != null
                        ? new ArrayList<>(workoutPlan.getTrainingStyle())
                        : null
        );
    }
}
