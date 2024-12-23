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
        System.out.println("Starting exercise population...");

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

            System.out.println("HTTP response received. Status code: " + response.statusCode());

            if (response.statusCode() == 200) {
                System.out.println("Parsing exercises...");
                List<ApiExercise> apiExercises = parseApiExercises(response.body());
                System.out.println("Number of exercises fetched: " + apiExercises.size());

                if (apiExercises.isEmpty()) {
                    System.err.println("No exercises were fetched. Please check the API response.");
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
                        } else {
                            System.err.println("Unknown muscle group: " + apiExercise.getTarget());
                        }

                        exercises.add(exercise);
                        System.out.println("Exercise created: " + exercise.getName() + " | Muscle Group: " + apiExercise.getTarget());
                    } catch (Exception ex) {
                        System.err.println("Error creating exercise for API data: " + apiExercise);
                        ex.printStackTrace();
                    }
                }


                // Save exercises to the database
                List<Exercise> savedExercises = new ArrayList<>();
                System.out.println("Saving exercises to the database...");
                exercises.forEach(exercise -> {
                    Exercise savedExercise = exerciseService.createExercise(exercise); // Save the exercise
                    savedExercises.add(savedExercise); // Add the saved exercise to the list
                });
                if (savedExercises.isEmpty()) {
                    System.err.println("No exercises were saved. Please check the API response.");
                }
                else{
                    System.out.println("All exercises saved successfully.");
                }

            } else {
                System.err.println("Failed to fetch exercises. HTTP Status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error occurred during exercise population.");
            e.printStackTrace();
        }
    }

    private List<ApiExercise> parseApiExercises(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown fields
        try {
            return objectMapper.readValue(responseBody, new TypeReference<List<ApiExercise>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing API response to ApiExercise objects.");
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
        System.err.println("Unknown muscle group: " + target);
        return MuscleGroup.UNKNOWN; // Assign a default value if applicable
    }

}
