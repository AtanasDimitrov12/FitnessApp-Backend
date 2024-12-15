package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.domain.WorkoutPlan;

public interface WorkoutPlanGenerator {
    WorkoutPlan calculateWorkoutPlan(UserWorkoutPreference workoutPreference);
}
