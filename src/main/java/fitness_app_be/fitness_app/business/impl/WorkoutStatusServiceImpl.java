package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.NotificationService;
import fitness_app_be.fitness_app.business.WorkoutStatusService;
import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.domain.WorkoutStatus;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WorkoutStatusServiceImpl implements WorkoutStatusService {

    private final WorkoutStatusRepository workoutStatusRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public WorkoutStatus findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId) {
        return workoutStatusRepository.findByWorkoutPlanIdAndWorkoutId(workoutPlanId, workoutId)
                .orElseThrow(() -> new RuntimeException("Workout status not found"));
    }

    @Transactional
    @Override
    public Notification markWorkoutAsDone(Long workoutPlanId, Long workoutId, Long userId) {
        // Retrieve workout status
        WorkoutStatus status = workoutStatusRepository
                .findByWorkoutPlanIdAndWorkoutId(workoutPlanId, workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found in the plan"));

        // Mark as done and update the repository
        status.setIsDone(true);
        workoutStatusRepository.update(status);

        // Calculate remaining workouts and send a notification
        int remainingWorkouts = getRemainingWorkoutsForPlan(workoutPlanId);
        String message = remainingWorkouts == 0
                ? "Nice job! You finished all your workouts for the week!"
                : "Nice job! Finish your workout. You have " + remainingWorkouts + " more to go.";

        return notificationService.createNotification(userId, message);
    }

    private int getRemainingWorkoutsForPlan(Long workoutPlanId) {
        return (int) workoutStatusRepository.findByWorkoutPlanId(workoutPlanId).stream()
                .filter(status -> !status.getIsDone())
                .count();
    }

    @Transactional
    @Override
    public void resetWeeklyWorkouts(int currentWeekNumber) {
        // Fetch all workout statuses and reset them
        List<WorkoutStatus> statuses = workoutStatusRepository.findAll();
        statuses.forEach(status -> {
            status.setIsDone(false);
            status.setWeekNumber(currentWeekNumber);
        });

        // Save the updated statuses
        workoutStatusRepository.saveAll(statuses);
    }

    @Scheduled(cron = "0 0 0 * * MON") // Runs every Monday at 00:00
    @Transactional
    @Override
    public void resetWeeklyWorkouts() {
        // Determine the current week number
        int currentWeekNumber = LocalDate.now()
                .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        // Delegate to the parameterized reset method
        resetWeeklyWorkouts(currentWeekNumber);
    }

    @Override
    public List<WorkoutStatus> getWorkoutStatusesForPlan(Long workoutPlanId) {
        return workoutStatusRepository.findByWorkoutPlanId(workoutPlanId);
    }

    @Override
    public void save(WorkoutStatus workoutStatus) {
        workoutStatusRepository.create(workoutStatus);
    }

    @Override
    public Long getCompletedWorkouts(Long userId, String rangeType) {
        int currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int startWeek = calculateStartWeek(currentWeek, rangeType);

        return workoutStatusRepository.countCompletedWorkoutsByWeekRange(userId, startWeek, currentWeek);
    }

    private int calculateStartWeek(int currentWeek, String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "month":
                return currentWeek - (LocalDate.now().getDayOfMonth() / 7); // Approximate start week of the month
            case "quarter":
                return currentWeek - (currentWeek - 1) % 13; // Start week of the current quarter
            case "year":
                return 1; // Start week of the year
            default:
                throw new IllegalArgumentException("Invalid range type. Must be 'month', 'quarter', or 'year'.");
        }
    }
}
