package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.WorkoutStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-status")
@RequiredArgsConstructor
public class WorkoutStatusController {

    private final WorkoutStatusService workoutStatusService;

    /**
     * Marks a workout as done for a specific user.
     *
     * @param workoutPlanId ID of the workout plan
     * @param workoutId     ID of the workout
     * @param userId        ID of the user
     * @return ResponseEntity with success message
     */
    @PostMapping("/mark-done")
    public ResponseEntity<String> markWorkoutAsDone(
            @RequestParam Long workoutPlanId,
            @RequestParam Long workoutId,
            @RequestParam Long userId) {

        workoutStatusService.markWorkoutAsDone(workoutPlanId, workoutId, userId);
        return ResponseEntity.ok("Workout marked as done and notification sent.");
    }
}
