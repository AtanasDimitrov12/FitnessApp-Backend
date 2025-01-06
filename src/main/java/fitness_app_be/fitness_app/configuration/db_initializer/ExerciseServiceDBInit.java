package fitness_app_be.fitness_app.configuration.db_initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiExercise;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseServiceDBInit {

    private final ExerciseService exerciseService; // Your Exercise domain service

    public void populateExercises() {

        String apiUrl = "https://exercisedb.p.rapidapi.com/exercises?limit=10&offset=0"; // Base API URL
        String apiKey = "6fd1c5fd02msh3d227efa76d3cd0p16f50ejsnd74c546bb74c"; // Replace with your API key

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

                if (apiExercises.isEmpty()) {
                    return;
                }

                List<Exercise> exercises = new ArrayList<>();
                for (ApiExercise apiExercise : apiExercises) {
                    try {
                        Exercise exercise = new Exercise();
                        exercise.setName(apiExercise.getName());
                        exercise.setSets(3); // Default value
                        exercise.setReps(12); // Default value

                        // Map target muscle group string to the MuscleGroup enum
                        MuscleGroup muscleGroup = mapToMuscleGroup(apiExercise.getTarget());
                        if (muscleGroup != null) {
                            exercise.setMuscleGroup(muscleGroup);
                        }

                        exercises.add(exercise);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                // Save exercises to the database
                List<Exercise> savedExercises = new ArrayList<>();
                exercises.forEach(exercise -> {
                    Exercise savedExercise = exerciseService.createExercise(exercise); // Save the exercise
                    savedExercises.add(savedExercise); // Add the saved exercise to the list
                });


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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private MuscleGroup mapToMuscleGroup(String target) {
        for (MuscleGroup muscleGroup : MuscleGroup.values()) {
            if (muscleGroup.getDisplayName().equalsIgnoreCase(target)) {
                return muscleGroup;
            }
        }
        return MuscleGroup.UNKNOWN; // Assign a default value if applicable
    }

}
