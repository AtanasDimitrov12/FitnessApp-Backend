package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.controllers.dto.webSockets.WorkoutUpdateMessage;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WorkoutWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    public WorkoutWebSocketController(SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    // Mapping for admin updates to notify subscribers
    @MessageMapping("/update-workout")
    public Map<String, String> handleWorkoutUpdate(WorkoutUpdateMessage workoutUpdateMessage) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", "Workout \"" + workoutUpdateMessage.getWorkoutName() + "\" has been updated by the admin.");

        // Retrieve all users who have this workout in their plans
        List<Long> userIds = userService.getUsersWithWorkout(workoutUpdateMessage.getWorkoutId());


        // Send notification to each user's WebSocket topic
        userIds.forEach(userId ->
                messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification)
        );

        return notification;
    }
}
