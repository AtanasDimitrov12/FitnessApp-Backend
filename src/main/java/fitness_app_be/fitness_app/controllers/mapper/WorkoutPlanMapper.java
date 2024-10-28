package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkoutPlanMapper {

    private final WorkoutMapper workoutMapper;

    public WorkoutPlanMapper(WorkoutMapper workoutMapper)
    {
        this.workoutMapper = workoutMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanDTO workoutPlanDTO) {
        return new WorkoutPlan(workoutPlanDTO.getId(), workoutPlanDTO.getUserId(),
                workoutPlanDTO.getWorkouts().stream().map(workoutMapper::toDomain).collect(Collectors.toList()));
    }

    public WorkoutPlanDTO domainToDto(WorkoutPlan workoutPlan) {
        return new WorkoutPlanDTO(workoutPlan.getId(), workoutPlan.getUserId(),
                workoutPlan.getWorkouts().stream().map(workoutMapper::domainToDto).collect(Collectors.toList()));
    }
}
