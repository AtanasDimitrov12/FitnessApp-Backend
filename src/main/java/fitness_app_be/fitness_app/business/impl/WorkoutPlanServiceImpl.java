package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.business.WorkoutStatusService;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.domain.WorkoutStatus;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutService workoutService;
    private final WorkoutStatusService workoutStatusService;

    @Override
    public List<WorkoutPlan> getAllWorkoutPlans(){
        return workoutPlanRepository.getAll();
    }

    @Override
    public WorkoutPlan getWorkoutPlanById(Long id) {
        return workoutPlanRepository.getWorkoutPlanById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(id));
    }

    @Override
    public WorkoutPlan getWorkoutPlanByUserId(Long userId) {
        return workoutPlanRepository.getWorkoutPlanByUserId(userId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(userId));
    }

    @Override
    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        // Ensure all workouts in the plan are managed
        workoutPlan.setWorkouts(
                workoutPlan.getWorkouts().stream()
                        .map(workout -> {
                            if (workout.getId() != null) {
                                return workoutService.getWorkoutById(workout.getId()); // Fetch managed workout
                            }
                            return workout; // New workout, no need to fetch
                        })
                        .toList()
        );

        // Save the workout plan
        WorkoutPlan savedPlan = workoutPlanRepository.create(workoutPlan);

        // Get the current week number
        int currentWeekNumber = java.time.LocalDate.now().get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        // Initialize and save WorkoutStatus for each workout in the plan
        for (Workout workout : savedPlan.getWorkouts()) {
            WorkoutStatus status = new WorkoutStatus();
            status.setWorkoutPlan(savedPlan); // Associate with the plan
            status.setWorkout(workout); // Associate with the workout
            status.setIsDone(false); // Default is not done
            status.setWeekNumber(currentWeekNumber); // Set the current week number
            workoutStatusService.save(status); // Save each WorkoutStatus individually
        }

        return savedPlan;
    }




    @Override
    public void deleteWorkoutPlan(Long id) {
        if (!workoutPlanRepository.exists(id)) {
            throw new WorkoutPlanNotFoundException(id);
        }
        workoutPlanRepository.delete(id);
    }

    @Override
    @Transactional
    public WorkoutPlan updateWorkoutPlan(WorkoutPlan workoutPlan) {
        // Fetch the existing workout plan from the database (managed entity)
        WorkoutPlan existingPlan = workoutPlanRepository.getWorkoutPlanById(workoutPlan.getId())
                .orElseThrow(() -> new EntityNotFoundException("WorkoutPlan with ID " + workoutPlan.getId() + " not found"));

        // Ensure all workouts in the plan are managed
        List<Workout> managedWorkouts = workoutPlan.getWorkouts().stream()
                .map(workout -> {
                    if (workout.getId() != null) {
                        return workoutService.getWorkoutById(workout.getId()); // Fetch managed workout
                    }
                    return workout; // New workout, no need to fetch
                })
                .toList();

        existingPlan.setWorkouts(managedWorkouts);

        // **Delete existing workout statuses before creating new ones**
        workoutStatusService.deleteByWorkoutPlanId(existingPlan.getId());

        // Save the updated workout plan
        WorkoutPlan savedPlan = workoutPlanRepository.update(existingPlan);

        // Get the current week number
        int currentWeekNumber = java.time.LocalDate.now().get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        // **Create new workout statuses for each workout**
        List<WorkoutStatus> newStatuses = savedPlan.getWorkouts().stream()
                .map(workout -> {
                    WorkoutStatus status = new WorkoutStatus();
                    status.setWorkoutPlan(savedPlan);
                    status.setWorkout(workout);
                    status.setIsDone(false);
                    status.setWeekNumber(currentWeekNumber);
                    return status;
                })
                .toList();

        // Save all new statuses
        workoutStatusService.saveAll(newStatuses);

        return savedPlan;
    }



}
