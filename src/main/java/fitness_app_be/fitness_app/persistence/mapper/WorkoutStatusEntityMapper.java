package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.domain.WorkoutStatus;
import fitness_app_be.fitness_app.persistence.entity.NotificationEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutStatusEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WorkoutStatusEntityMapper {
    private WorkoutEntityMapper workoutEntityMapper;
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;

    @Autowired
    public WorkoutStatusEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapper, @Lazy WorkoutPlanEntityMapper workoutPlanEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
        this.workoutPlanEntityMapper = workoutPlanEntityMapper;
    }

    public WorkoutStatus toDomain(WorkoutStatusEntity workoutStatusEntity) {
        if (workoutStatusEntity == null) {
            return null;
        }

        return WorkoutStatus.builder()
                .id(workoutStatusEntity.getId())
                .workout(workoutEntityMapper.toDomain(workoutStatusEntity.getWorkout()))
                .workoutPlan(workoutPlanEntityMapper.toDomain(workoutStatusEntity.getWorkoutPlan()))
                .isDone(workoutStatusEntity.getIsDone())
                .weekNumber(workoutStatusEntity.getWeekNumber())
                .build();
    }

    public WorkoutStatusEntity toEntity(WorkoutStatus workoutStatus) {
        if (workoutStatus == null) {
            return null;
        }

        WorkoutStatusEntity workoutStatusEntity = new WorkoutStatusEntity();
        workoutStatusEntity.setId(workoutStatus.getId());
        workoutStatusEntity.setWorkout(workoutEntityMapper.toEntity(workoutStatus.getWorkout()));
        workoutStatusEntity.setWorkoutPlan(workoutPlanEntityMapper.toEntity(workoutStatus.getWorkoutPlan()));
        workoutStatusEntity.setIsDone(workoutStatus.getIsDone());
        workoutStatusEntity.setWeekNumber(workoutStatus.getWeekNumber());


        return workoutStatusEntity;
    }
}
