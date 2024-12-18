package fitness_app_be.fitness_app.configuration.db_initializer.dto;

import lombok.Data;

@Data
public class ApiExercise {
    private String name;          // Exercise name
    private String target;        // Target muscle group
    private String equipment;     // Equipment used
}
