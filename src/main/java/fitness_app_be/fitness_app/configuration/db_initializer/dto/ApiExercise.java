package fitness_app_be.fitness_app.configuration.db_initializer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiExercise {
    private String bodyPart; // Maps to "bodyPart"
    private String equipment; // Maps to "equipment"
    private String gifUrl; // Maps to "gifUrl"
    private String id; // Maps to "id"
    private String name; // Maps to "name"
    private String target; // Maps to "target"
    private List<String> secondaryMuscles; // Maps to "secondaryMuscles"
    private List<String> instructions; // Maps to "instructions"     // Equipment used
}
