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
import java.time.LocalDateTime;
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

    @Override
    public Notification markWorkoutAsDone(Long workoutPlanId, Long workoutId, Long userId) {
        // Find WorkoutStatus
        WorkoutStatus status = workoutStatusRepository
                .findByWorkoutPlanIdAndWorkoutId(workoutPlanId, workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found in the plan"));

        // Update isDone status
        status.setIsDone(true);
        workoutStatusRepository.update(status);

        // Calculate remaining workouts
        int remainingWorkouts = getRemainingWorkoutsForPlan(workoutPlanId);


        // Save the notification to the database
        return notificationService.createNotification(userId, remainingWorkouts == 0
                ? "Nice job! You finished all your workouts for the week!"
                : "Nice job! Finish your workout. You have " + remainingWorkouts + " more to go.");
    }

    private int getRemainingWorkoutsForPlan(Long workoutPlanId) {
        List<WorkoutStatus> statuses = workoutStatusRepository.findByWorkoutPlanId(workoutPlanId);
        return (int) statuses.stream().filter(status -> !status.getIsDone()).count();
    }

    @Override
    @Transactional
    public void resetWeeklyWorkouts(int currentWeekNumber) {
        List<WorkoutStatus> statuses = workoutStatusRepository.findAll();

        statuses.forEach(status -> {
            status.setIsDone(false);
            status.setWeekNumber(currentWeekNumber);
        });

        workoutStatusRepository.saveAll(statuses);
        System.out.println("All workout statuses reset for the new week: " + currentWeekNumber);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * MON") // Runs every Monday at 00:00
    @Transactional
    public void resetWeeklyWorkouts() {
        int currentWeekNumber = LocalDate.now()
                .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        resetWeeklyWorkouts(currentWeekNumber);
    }



    @Override
    public List<WorkoutStatus> getWorkoutStatusesForPlan(Long workoutPlanId) {
        return workoutStatusRepository.findByWorkoutPlanId(workoutPlanId);
    }
}
