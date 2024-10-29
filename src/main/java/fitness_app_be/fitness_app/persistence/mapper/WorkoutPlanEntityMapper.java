package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                workoutPlanEntity.getUsers() != null
                        ? workoutPlanEntity.getUsers().stream()
                        .map(userEntityMapper::toDomain)
                        .collect(Collectors.toList())
                        : null,
                workoutPlanEntity.getWorkouts() != null
                        ? workoutPlanEntity.getWorkouts().stream()
                        .map(workoutEntityMapper::toDomain)
                        .collect(Collectors.toList())
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

        // Convert the list of users and workouts
        if (workoutPlan.getUsers() != null) {
            workoutPlanEntity.setUsers(
                    workoutPlan.getUsers().stream()
                            .map(userEntityMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (workoutPlan.getWorkouts() != null) {
            workoutPlanEntity.setWorkouts(
                    workoutPlan.getWorkouts().stream()
                            .map(workoutEntityMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        workoutPlanEntity.setFitnessGoals(workoutPlan.getFitnessGoals());
        workoutPlanEntity.setTrainingStyle(workoutPlan.getTrainingStyles());

        return workoutPlanEntity;
    }
}
