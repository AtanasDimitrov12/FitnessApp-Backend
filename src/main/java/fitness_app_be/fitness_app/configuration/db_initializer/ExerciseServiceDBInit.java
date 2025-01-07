package fitness_app_be.fitness_app.configuration.db_initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiExercise;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceDBInit {

    private final ExerciseService exerciseService;

    @Value("${apiUrl}")
    private String apiUrl;

    @Value("${apiKey}")
    private String apiKey;

    public void populateExercises() {
        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Create an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .GET()
                .build();

        try {
            // Send the HTTP request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ApiExercise> apiExercises = parseApiExercises(response.body());

                if (!apiExercises.isEmpty()) {
                    List<Exercise> exercises = mapApiExercisesToDomain(apiExercises);
                    saveExercises(exercises);
                }
            } else {
                System.err.println("Failed to fetch exercises. HTTP Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ApiExercise> parseApiExercises(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown fields
        try {
            return objectMapper.readValue(responseBody, new TypeReference<List<ApiExercise>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing API exercises: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Exercise> mapApiExercisesToDomain(List<ApiExercise> apiExercises) {
        List<Exercise> exercises = new ArrayList<>();
        for (ApiExercise apiExercise : apiExercises) {
            try {
                Exercise exercise = mapApiExerciseToDomain(apiExercise);
                exercises.add(exercise);
            } catch (Exception ex) {
                System.err.println("Error mapping exercise: " + apiExercise.getName() + ". " + ex.getMessage());
            }
        }
        return exercises;
    }

    private Exercise mapApiExerciseToDomain(ApiExercise apiExercise) {
        Exercise exercise = new Exercise();
        exercise.setName(apiExercise.getName());
        exercise.setSets(3); // Default value
        exercise.setReps(12); // Default value

        // Map target muscle group string to the MuscleGroup enum
        MuscleGroup muscleGroup = mapToMuscleGroup(apiExercise.getTarget());
        exercise.setMuscleGroup(Optional.ofNullable(muscleGroup).orElse(MuscleGroup.UNKNOWN));

        return exercise;
    }

    private MuscleGroup mapToMuscleGroup(String target) {
        for (MuscleGroup muscleGroup : MuscleGroup.values()) {
            if (muscleGroup.getDisplayName().equalsIgnoreCase(target)) {
                return muscleGroup;
            }
        }
        return MuscleGroup.UNKNOWN; // Assign a default value if applicable
    }

    private void saveExercises(List<Exercise> exercises) {
        exercises.forEach(exercise -> {
            try {
                exerciseService.createExercise(exercise);
            } catch (Exception e) {
                System.err.println("Error saving exercise: " + exercise.getName() + ". " + e.getMessage());
            }
        });
    }
}
