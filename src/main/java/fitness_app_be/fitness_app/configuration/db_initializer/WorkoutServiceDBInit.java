package fitness_app_be.fitness_app.configuration.db_initializer;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.FitnessGoal;
import fitness_app_be.fitness_app.domain.FitnessLevel;
import fitness_app_be.fitness_app.domain.TrainingStyle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class WorkoutServiceDBInit {

    private final WorkoutService workoutService; // Domain service for workouts
    private final ExerciseService exerciseService; // Domain service for exercises
    private final Random random = new Random(); // Random instance for tag selection

    public void populateWorkouts() throws IOException, InterruptedException {
        // Check if workouts already exist in the database
        long workoutCount = workoutService.getAllWorkouts().stream().count();
        if (workoutCount >= 40) {
            return;
        }

        // Wait until at least 10 exercises are available in the database
        List<Exercise> exercises = waitForExercisesToBeAvailable(10);
        if (exercises.size() < 10) {
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
                System.err.println("Failed to create workout: " + workout.getName());
                e.printStackTrace();
            }
        }
    }

    /**
     * Waits until there are at least the specified minimum number of exercises in the database.
     *
     * @param minExerciseCount Minimum number of exercises required.
     * @return List of exercises once the condition is met.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    private List<Exercise> waitForExercisesToBeAvailable(int minExerciseCount) throws InterruptedException {
        int elapsedSeconds = 0;
        List<Exercise> exercises;

        do {
            exercises = exerciseService.getAllExercises();
            if (exercises.size() >= minExerciseCount) {
                return exercises;
            }

            // Print a message every 10 seconds
            if (elapsedSeconds % 10 == 0) {
                System.out.println("Waiting for exercises to be available in the database...");
            }

            Thread.sleep(1000); // Wait 1 second before checking again
            elapsedSeconds++;
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
