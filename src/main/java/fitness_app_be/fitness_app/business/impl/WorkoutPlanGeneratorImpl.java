package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanGenerator;
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

        List<Workout> eligibleWorkouts = findMatchingWorkouts(workoutPreference);

        List<WorkoutPlan> generatedWorkoutPlans = generateWorkoutPlans(eligibleWorkouts, workoutPreference);

        return findBestWorkoutPlan(generatedWorkoutPlans, workoutPreference);
    }

    private List<Workout> findMatchingWorkouts(UserWorkoutPreference workoutPreference) {

        List<Workout> allWorkouts = workoutService.getAllWorkouts();

        return allWorkouts.stream()
                .filter(workout -> workoutPreference.getFitnessGoal() != null &&
                        workout.getFitnessGoals() != null &&
                        workout.getFitnessGoals().contains(workoutPreference.getFitnessGoal()))
                .filter(workout -> workoutPreference.getFitnessLevel() != null &&
                        workout.getFitnessLevels() != null &&
                        workout.getFitnessLevels().contains(workoutPreference.getFitnessLevel()))
                .filter(workout -> workoutPreference.getPreferredTrainingStyle() != null &&
                        workout.getTrainingStyles() != null &&
                        workout.getTrainingStyles().contains(workoutPreference.getPreferredTrainingStyle()))
                .toList();
    }


    private List<WorkoutPlan> generateWorkoutPlans(List<Workout> workouts, UserWorkoutPreference workoutPreference) {
        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        int daysAvailable = workoutPreference.getDaysAvailable();

        for (int i = 0; i < workouts.size(); i++) {
            List<Workout> planWorkouts = new ArrayList<>();
            int workoutDay = 0;

            for (int j = 0; j < daysAvailable && i + j < workouts.size(); j++) {
                Workout workout = workouts.get(i + j);
                planWorkouts.add(workout);

                workoutDay++;
                if (workoutDay >= daysAvailable) {
                    break;
                }
            }

            WorkoutPlan workoutPlan = new WorkoutPlan();
            workoutPlan.setWorkouts(planWorkouts);
            workoutPlans.add(workoutPlan);
        }
        return workoutPlans;
    }

    private WorkoutPlan findBestWorkoutPlan(List<WorkoutPlan> workoutPlans, UserWorkoutPreference workoutPreference) {

        return workoutPlans.stream()
                .min(Comparator.comparingInt(plan -> Math.abs(workoutPreference.getDaysAvailable() - plan.getWorkouts().size())))
                .orElse(null); // Return null if no workout plan is found
    }
}