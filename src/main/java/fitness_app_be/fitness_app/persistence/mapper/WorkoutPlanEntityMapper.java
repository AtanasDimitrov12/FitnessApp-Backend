package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class WorkoutPlanEntityMapper {

    private WorkoutEntityMapper workoutEntityMapper;

    @Autowired
    public WorkoutPlanEntityMapper(WorkoutEntityMapper workoutEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanEntity workoutPlanEntity) {
        if (workoutPlanEntity == null) {
            return null;
        }
        return new WorkoutPlan(
                workoutPlanEntity.getId(),
                workoutPlanEntity.getUser().getId(), // Use user entity’s ID
                workoutPlanEntity.getWorkouts().stream().map(workoutEntityMapper::toDomain).collect(Collectors.toList())
        );
    }

    public WorkoutPlanEntity toEntity(WorkoutPlan workoutPlan, UserEntity userEntity) {
        if (workoutPlan == null) {
            return null;
        }

        return new WorkoutPlanEntity(workoutPlan.getId(), userEntity, workoutPlan.getWorkouts().stream().map(workoutEntityMapper::toEntity).collect(Collectors.toList()));
    }
}
