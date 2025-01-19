package fitness_app_be.fitness_app.configuration.db_initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiExercise;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExerciseServiceDBInit {

    private final ExerciseService exerciseService;

    @Value("${apiUrl}")
    private String apiUrl;

    @Value("${apiKey}")
    private String apiKey;

    public void populateExercises() {

        long exerciseCount = exerciseService.getAllExercises().stream().count();
        if (exerciseCount >= 20) {
            log.info("Exercises already populated. Skipping initialization.");
            return;
        }
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            handleResponse(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            log.error("Thread interrupted while fetching exercises.", e);
        } catch (Exception e) {
            log.error("Error during API request to fetch exercises.", e);
        }
    }

    private void handleResponse(HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            List<ApiExercise> apiExercises = parseApiExercises(response.body());
            if (!apiExercises.isEmpty()) {
                List<Exercise> exercises = mapApiExercisesToDomain(apiExercises);
                saveExercises(exercises);
            } else {
                log.warn("No exercises found in the API response.");
            }
        } else {
            log.error("Failed to fetch exercises. HTTP Status: {}", response.statusCode());
        }
    }

    private List<ApiExercise> parseApiExercises(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(responseBody, new TypeReference<List<ApiExercise>>() {});
        } catch (Exception e) {
            log.error("Error parsing API exercises.", e);
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
                log.warn("Error mapping exercise: {}. Skipping. Details: {}", apiExercise.getName(), ex.getMessage());
            }
        }
        return exercises;
    }

    private Exercise mapApiExerciseToDomain(ApiExercise apiExercise) {
        Exercise exercise = new Exercise();
        exercise.setName(apiExercise.getName());
        exercise.setSets(3); // Default value
        exercise.setReps(12); // Default value
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
        return MuscleGroup.UNKNOWN;
    }

    private void saveExercises(List<Exercise> exercises) {
        exercises.forEach(exercise -> {
            try {
                exerciseService.createExercise(exercise);
                log.info("Saved exercise: {}", exercise.getName());
            } catch (Exception e) {
                log.error("Error saving exercise: {}. Details: {}", exercise.getName(), e.getMessage());
            }
        });
    }
}
