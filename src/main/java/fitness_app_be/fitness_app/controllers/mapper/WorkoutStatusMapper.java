package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.NotificationDTO;
import fitness_app_be.fitness_app.controllers.dto.WorkoutStatusDTO;
import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.domain.WorkoutStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class WorkoutStatusMapper {

    private final WorkoutMapper workoutMapper;
    private final WorkoutPlanMapper workoutPlanMapper;

    @Autowired
    public WorkoutStatusMapper(@Lazy WorkoutMapper workoutMapper, @Lazy WorkoutPlanMapper workoutPlanMapper) {
        this.workoutMapper = workoutMapper;
        this.workoutPlanMapper = workoutPlanMapper;
    }

    public WorkoutStatus toDomain(WorkoutStatusDTO workoutStatusDTO) {
        if (workoutStatusDTO == null) {
            return null;
        }

        return new WorkoutStatus(
                workoutStatusDTO.getId(),
                workoutPlanMapper.toDomain(workoutStatusDTO.getWorkoutPlan()),
                workoutMapper.toDomain(workoutStatusDTO.getWorkout()),
                workoutStatusDTO.getIsDone(),
                workoutStatusDTO.getWeekNumber()
        );
    }

    public WorkoutStatusDTO domainToDto(WorkoutStatus workoutStatus) {
        if (workoutStatus == null) {
            return null;
        }

        return new WorkoutStatusDTO(
                workoutStatus.getId(),
                workoutPlanMapper.domainToDto(workoutStatus.getWorkoutPlan()),
                workoutMapper.domainToDto(workoutStatus.getWorkout()),
                workoutStatus.getIsDone(),
                workoutStatus.getWeekNumber()
        );
    }
}
