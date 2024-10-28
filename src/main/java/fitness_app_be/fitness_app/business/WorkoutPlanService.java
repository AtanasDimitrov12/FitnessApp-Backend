package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.WorkoutPlan;

import java.util.List;

public interface WorkoutPlanService {
    List<WorkoutPlan> getAllWorkoutPlans();
    WorkoutPlan getWorkoutPlanById(Long id);
    WorkoutPlan getWorkoutPlanByUserId(Long userId);
    WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan);
    void deleteWorkoutPlan(Long id);
    WorkoutPlan updateWorkoutPlan(WorkoutPlan workoutPlan);
}
