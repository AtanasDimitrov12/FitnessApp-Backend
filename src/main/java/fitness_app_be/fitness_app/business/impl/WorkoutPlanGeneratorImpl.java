package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanGenerator;
import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutPlanGeneratorImpl implements WorkoutPlanGenerator {

    private final WorkoutService workoutService;

    @Override
    public WorkoutPlan calculateWorkoutPlan(UserWorkoutPreference workoutPreference) {
        // Step 1: Fetch all eligible workouts
        List<Workout> eligibleWorkouts = findMatchingWorkouts(workoutPreference);

        // Step 2: Create workout plans based on eligible workouts
        List<WorkoutPlan> generatedWorkoutPlans = generateWorkoutPlans(eligibleWorkouts, workoutPreference);

        // Step 3: Find and return the best matching workout plan
        return findBestWorkoutPlan(generatedWorkoutPlans, workoutPreference);
    }

    private List<Workout> findMatchingWorkouts(UserWorkoutPreference workoutPreference) {
        // Fetch all workouts from the service
        List<Workout> allWorkouts = workoutService.getAllWorkouts();

        // Filter workouts based on user preferences
        return allWorkouts.stream()
                .filter(workout -> workout.getFitnessGoals().contains(workoutPreference.getFitnessGoal()))
                .filter(workout -> workout.getFitnessLevels().contains(workoutPreference.getFitnessLevel()))
                .filter(workout -> workout.getTrainingStyles().contains(workoutPreference.getPreferredTrainingStyle()))
                .toList();
    }

    private List<WorkoutPlan> generateWorkoutPlans(List<Workout> workouts, UserWorkoutPreference workoutPreference) {
        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        int daysAvailable = workoutPreference.getDaysAvailable();

        // Generate combinations of workouts to create workout plans
        for (int i = 0; i < workouts.size(); i++) {
            List<Workout> planWorkouts = new ArrayList<>();
            int workoutDay = 0;

            for (int j = 0; j < daysAvailable && i + j < workouts.size(); j++) {
                Workout workout = workouts.get(i + j);
                planWorkouts.add(workout);

                // Move to the next workout day
                workoutDay++;
                if (workoutDay >= daysAvailable) {
                    break;
                }
            }

            // Create a workout plan with the selected workouts
            WorkoutPlan workoutPlan = new WorkoutPlan();
            workoutPlan.setWorkouts(planWorkouts);
            workoutPlans.add(workoutPlan);
        }
        return workoutPlans;
    }

    private WorkoutPlan findBestWorkoutPlan(List<WorkoutPlan> workoutPlans, UserWorkoutPreference workoutPreference) {
        // Find the single best workout plan based on alignment with user's available days
        return workoutPlans.stream()
                .min(Comparator.comparingInt(plan -> Math.abs(workoutPreference.getDaysAvailable() - plan.getWorkouts().size())))
                .orElse(null); // Return null if no workout plan is found
    }
}