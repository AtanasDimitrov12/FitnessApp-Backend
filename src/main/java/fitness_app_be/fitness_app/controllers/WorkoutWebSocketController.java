package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.controllers.dto.webSockets.WorkoutUpdateMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WorkoutWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WorkoutWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Mapping for admin updates to notify subscribers
    @MessageMapping("/update-workout")
    @SendTo("/topic/workouts")
    public String handleWorkoutUpdate(WorkoutUpdateMessage workoutUpdateMessage) {
        // Send notification to subscribed clients
        String notificationMessage = "Workout \"" + workoutUpdateMessage.getWorkoutName() + "\" has been updated by the admin.";
        return notificationMessage;
    }
}

