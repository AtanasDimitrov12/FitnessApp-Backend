package fitness_app_be.fitness_app.configuration.db_initializer;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiExercise;
import fitness_app_be.fitness_app.domain.Exercise;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseServiceDBInit {

    private final ExerciseService exerciseService; // Your Exercise domain service
    private final RestTemplate restTemplate;

    public void populateExercises() {
        long exerciseCount = exerciseService.getAllExercises().stream().count();
        if (exerciseCount >= 10) { // Avoid fetching if exercises already populated
            return;
        }

        String apiUrl = "https://exercisedb.p.rapidapi.com/exercises";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "6fd1c5fd02msh3d227efa76d3cd0p16f50ejsnd74c546bb74c"); // Replace with your API key
        headers.set("X-RapidAPI-Host", "exercisedb.p.rapidapi.com");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiExercise[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ApiExercise[].class
        );

        if (response.getBody() != null) {
            List<ApiExercise> apiExercises = List.of(response.getBody());
            List<Exercise> exercises = new ArrayList<>();

            for (ApiExercise apiExercise : apiExercises) {
                Exercise exercise = new Exercise();
                exercise.setName(apiExercise.getName());
                exercise.setMuscleGroup(apiExercise.getTarget());
                exercise.setSets(3); // Default value
                exercise.setReps(12); // Default value
                exercises.add(exercise);
            }

            // Save exercises to the database
            exercises.forEach(exerciseService::createExercise);
        }
    }
}

