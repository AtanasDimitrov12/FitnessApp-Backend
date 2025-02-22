package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.domain.WorkoutStatus;

import java.util.List;

public interface WorkoutStatusService {
    WorkoutStatus findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId);
    Notification markWorkoutAsDone(Long workoutPlanId, Long workoutId, Long userId);
    void resetWeeklyWorkouts(int currentWeekNumber);
    void resetWeeklyWorkouts();
    List<WorkoutStatus> getWorkoutStatusesForPlan(Long workoutPlanId);
    boolean isWorkoutDone(Long workoutPlanId, Long workoutId);
    void save(WorkoutStatus workoutStatus);
    Long getCompletedWorkouts(Long userId, String rangeType);
    void deleteByWorkoutPlanId(Long workoutPlanId);
    void saveAll(List<WorkoutStatus> workoutStatuses);

}
