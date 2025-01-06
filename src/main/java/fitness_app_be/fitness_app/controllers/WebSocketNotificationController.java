package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.NotificationService;
import fitness_app_be.fitness_app.business.WorkoutStatusService;
import fitness_app_be.fitness_app.controllers.dto.websockets.WorkoutDonePayload;
import fitness_app_be.fitness_app.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;



@Controller
@RequiredArgsConstructor
public class WebSocketNotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WorkoutStatusService workoutStatusService;
    private final NotificationService notificationService;

    /**
     * WebSocket endpoint for marking a workout as done.
     * Clients send messages to "/app/mark-workout-done".
     *
     * @param payload Contains workoutPlanId, workoutId, and userId.
     */
    @MessageMapping("/mark-workout-done")
    public void markWorkoutAsDone(WorkoutDonePayload payload) {
        // Use WorkoutStatusService to mark the workout as done
        workoutStatusService.markWorkoutAsDone(payload.getWorkoutPlanId(), payload.getWorkoutId(), payload.getUserId());

        // Fetch updated notifications for the user
        Notification notification = workoutStatusService.markWorkoutAsDone(payload.getWorkoutPlanId(), payload.getWorkoutId(), payload.getUserId());

        // Send the notification to the specific user over WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + payload.getUserId(), notification);
    }
}


