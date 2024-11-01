package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.WorkoutPlan;

import java.util.List;
import java.util.Optional;

public interface WorkoutPlanRepository {
    boolean exists(long id);

    List<WorkoutPlan> getAll();

    WorkoutPlan create(WorkoutPlan workoutPlan);

    WorkoutPlan update(WorkoutPlan workoutPlan);

    void delete(long workoutPlanId);

    Optional<WorkoutPlan> getWorkoutPlanById(long workoutPlanId);
    Optional<WorkoutPlan> getWorkoutPlanByUserId(long userId);
}
