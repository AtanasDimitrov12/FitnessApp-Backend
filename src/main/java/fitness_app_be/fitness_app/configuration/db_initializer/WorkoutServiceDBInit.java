package fitness_app_be.fitness_app.configuration.db_initializer;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j // Use Lombok for logging
public class WorkoutServiceDBInit {

    private final WorkoutService workoutService; // Domain service for workouts
    private final ExerciseService exerciseService;
    private final SecureRandom random = new SecureRandom(); // Random instance for tag selection

    public void populateWorkouts() throws IOException, InterruptedException {
        // Check if workouts already exist in the database
        long workoutCount = workoutService.getAllWorkouts().stream().count();
        if (workoutCount >= 40) {
            log.info("Workouts already populated. Skipping initialization.");
            return;
        }

        // Wait until at least 10 exercises are available in the database
        List<Exercise> exercises = waitForExercisesToBeAvailable(10);
        if (exercises.size() < 10) {
            log.error("Insufficient exercises found in the database. Populate exercises first!");
            throw new IllegalStateException("Insufficient exercises found in the database. Populate exercises first!");
        }

        List<Workout> workouts = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Workout workout = new Workout();
            workout.setName("Workout " + (i + 1));
            workout.setDescription("A challenging workout to improve your fitness.");

            List<Exercise> randomExercises = getRandomExercises(exercises, 5);
            workout.setExercises(randomExercises);

            // Assign random tags
            workout.setFitnessGoals(getRandomFitnessGoals());
            workout.setFitnessLevels(getRandomFitnessLevels());
            workout.setTrainingStyles(getRandomTrainingStyles());

            workouts.add(workout);
        }

        File defaultImageFile = new File("src/main/resources/images/default_workout.png");

        for (Workout workout : workouts) {
            try {
                workoutService.createWorkout(workout, defaultImageFile);
            } catch (IOException e) {
                log.error("Error creating workout: {}. Error: {}", workout.getName(), e.getMessage());
                log.debug("Stack trace: ", e); // Use debug for the full stack trace
            }
        }
    }

    private List<Exercise> waitForExercisesToBeAvailable(int minExerciseCount) throws InterruptedException {
        List<Exercise> exercises;

        do {
            exercises = exerciseService.getAllExercises();
            if (exercises.size() >= minExerciseCount) {
                return exercises;
            }

            log.info("Waiting for exercises to populate in the database...");
            Thread.sleep(1000); // Wait 1 second before checking again
        } while (true); // Continue indefinitely until the condition is met
    }

    private List<Exercise> getRandomExercises(List<Exercise> exercises, int count) {
        List<Exercise> randomExercises = new ArrayList<>(exercises);
        Collections.shuffle(randomExercises); // Shuffle the list
        return randomExercises.subList(0, Math.min(count, randomExercises.size())); // Get the first `count` exercises
    }

    private List<FitnessGoal> getRandomFitnessGoals() {
        FitnessGoal[] goals = FitnessGoal.values();
        return List.of(goals[random.nextInt(goals.length)]); // Single random fitness goal
    }

    private List<FitnessLevel> getRandomFitnessLevels() {
        FitnessLevel[] levels = FitnessLevel.values();
        return List.of(levels[random.nextInt(levels.length)]); // Single random fitness level
    }

    private List<TrainingStyle> getRandomTrainingStyles() {
        TrainingStyle[] styles = TrainingStyle.values();
        return List.of(styles[random.nextInt(styles.length)]); // Single random training style
    }
}
