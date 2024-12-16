package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class WorkoutPlanEntityMapper {

    private WorkoutEntityMapper workoutEntityMapper;

    @Autowired
    public WorkoutPlanEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanEntity workoutPlanEntity) {
        if (workoutPlanEntity == null) {
            return null;
        }

        return new WorkoutPlan(
                workoutPlanEntity.getId(),
                workoutPlanEntity.getUserId(),
                workoutPlanEntity.getWorkouts() != null
                        ? workoutPlanEntity.getWorkouts().stream()
                        .map(workoutEntityMapper::toDomain)
                        .toList()
                        : null
        );
    }

    public WorkoutPlanEntity toEntity(WorkoutPlan workoutPlan) {
        if (workoutPlan == null) {
            return null;
        }

        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(workoutPlan.getId());
        workoutPlanEntity.setUserId(workoutPlan.getUserId());



        if (workoutPlan.getWorkouts() != null) {
            workoutPlanEntity.setWorkouts(
                    workoutPlan.getWorkouts().stream()
                            .map(workoutEntityMapper::toEntity)
                            .toList()
            );
        }

        return workoutPlanEntity;
    }
}
