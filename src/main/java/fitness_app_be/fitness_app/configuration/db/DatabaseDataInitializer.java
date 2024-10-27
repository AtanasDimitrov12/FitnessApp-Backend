package fitness_app_be.fitness_app.configuration.db;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.repositories.TrainerRepository;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseDataInitializer {

    private final TrainerRepository trainerRepository;
    private final WorkoutRepository workoutRepository;

    public DatabaseDataInitializer(WorkoutRepository workoutRepository, TrainerRepository trainerRepository) {
        this.workoutRepository = workoutRepository;
        this.trainerRepository = trainerRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {

        // Create and save a new Trainer
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setExpertise("Strength Training"); // Set any other required fields for TrainerEntity
        Trainer savedTrainer = trainerRepository.create(trainer); // Save trainer and get saved entity
        Long trainerId = savedTrainer.getId(); // Retrieve the generated ID

        // List of exercises
        List<String> exercisesList = new ArrayList<>();
        exercisesList.add("BURPEE");
        exercisesList.add("BENCH PRESS");
        exercisesList.add("DEADLIFT");
        exercisesList.add("ROWING");

        // Create workouts with the trainer's ID
        workoutRepository.create(createNewWorkout(trainerId, "Full Body Workout", "A complete workout for all major muscle groups.", "/images/workout-card.jpg", exercisesList));
        workoutRepository.create(createNewWorkout(trainerId, "Flexibility and Stretching", "Improves flexibility and range of motion.", "/images/workout-card.jpg", exercisesList));
        workoutRepository.create(createNewWorkout(trainerId, "Back and Shoulders", "Strengthens the back and shoulder muscles.", "/images/workout-card.jpg", exercisesList));
        workoutRepository.create(createNewWorkout(trainerId, "Leg Day", "Focuses on building strength in the lower body.", "/images/workout-card.jpg", exercisesList));
    }

    private Workout createNewWorkout(Long trainerId, String name, String description, String pictureURL, List<String> exercises) {
        Workout workout = new Workout();
        workout.setTrainerId(trainerId);
        workout.setName(name);
        workout.setDescription(description);
        workout.setPictureURL(pictureURL);
        workout.setExercises(exercises);
        return workout;
    }
}
