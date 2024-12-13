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
    private UserEntityMapper userEntityMapper;

    @Autowired
    public WorkoutPlanEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapper, @Lazy UserEntityMapper userEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }

    public WorkoutPlan toDomain(WorkoutPlanEntity workoutPlanEntity) {
        if (workoutPlanEntity == null) {
            return null;
        }

        return new WorkoutPlan(
                workoutPlanEntity.getId(),
                workoutPlanEntity.getWorkouts() != null
                        ? workoutPlanEntity.getWorkouts().stream()
                        .map(workoutEntityMapper::toDomain)
                        .toList()
                        : null,
                workoutPlanEntity.getFitnessGoals(),
                workoutPlanEntity.getTrainingStyle()
        );
    }

    public WorkoutPlanEntity toEntity(WorkoutPlan workoutPlan) {
        if (workoutPlan == null) {
            return null;
        }

        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(workoutPlan.getId());



        if (workoutPlan.getWorkouts() != null) {
            workoutPlanEntity.setWorkouts(
                    workoutPlan.getWorkouts().stream()
                            .map(workoutEntityMapper::toEntity)
                            .toList()
            );
        }

        workoutPlanEntity.setFitnessGoals(workoutPlan.getFitnessGoals());
        workoutPlanEntity.setTrainingStyle(workoutPlan.getTrainingStyles());

        return workoutPlanEntity;
    }
}
