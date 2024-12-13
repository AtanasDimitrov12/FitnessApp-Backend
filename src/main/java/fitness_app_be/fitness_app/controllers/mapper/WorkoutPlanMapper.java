package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WorkoutPlanMapper {

    private final WorkoutMapper workoutMapper;
    private final UserMapper userMapper;

    @Autowired
    public WorkoutPlanMapper(@Lazy WorkoutMapper workoutMapper, @Lazy UserMapper userMapper) {
        this.workoutMapper = workoutMapper;
        this.userMapper = userMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanDTO workoutPlanDTO) {
        if (workoutPlanDTO == null) {
            return null;
        }

        return new WorkoutPlan(
                workoutPlanDTO.getId(),
                workoutPlanDTO.getWorkouts() != null
                        ? workoutPlanDTO.getWorkouts().stream()
                        .map(workoutMapper::toDomain)
                        .toList()
                        : null,
                workoutPlanDTO.getFitnessGoals() != null
                        ? new ArrayList<>(workoutPlanDTO.getFitnessGoals())
                        : null,
                workoutPlanDTO.getTrainingStyles() != null
                        ? new ArrayList<>(workoutPlanDTO.getTrainingStyles())
                        : null
        );
    }

    public WorkoutPlanDTO domainToDto(WorkoutPlan workoutPlan) {
        if (workoutPlan == null) {
            return null;
        }

        return new WorkoutPlanDTO(
                workoutPlan.getId(),
                workoutPlan.getWorkouts() != null
                        ? workoutPlan.getWorkouts().stream()
                        .map(workoutMapper::domainToDto)
                        .toList()
                        : null,
                workoutPlan.getFitnessGoals() != null
                        ? new ArrayList<>(workoutPlan.getFitnessGoals())
                        : null,
                workoutPlan.getTrainingStyles() != null
                        ? new ArrayList<>(workoutPlan.getTrainingStyles())
                        : null
        );
    }
}
