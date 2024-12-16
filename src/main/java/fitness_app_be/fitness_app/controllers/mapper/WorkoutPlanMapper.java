package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class WorkoutPlanMapper {

    private final WorkoutMapper workoutMapper;

    @Autowired
    public WorkoutPlanMapper(@Lazy WorkoutMapper workoutMapper) {
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
                        .toList()
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
                        .toList()
                        : null
        );
    }
}
