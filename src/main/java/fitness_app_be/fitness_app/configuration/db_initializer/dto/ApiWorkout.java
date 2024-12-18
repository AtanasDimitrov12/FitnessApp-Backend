package fitness_app_be.fitness_app.configuration.db_initializer.dto;

import lombok.Data;

@Data
public class ApiWorkout {
    private String name;               // Workout name
    private String description;        // Workout description
    private String pictureUrl;         // URL of an image representing the workout
}

