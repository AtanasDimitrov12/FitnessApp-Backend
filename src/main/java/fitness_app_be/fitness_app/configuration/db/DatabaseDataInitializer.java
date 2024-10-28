package fitness_app_be.fitness_app.configuration.db;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.repositories.AdminRepository;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseDataInitializer {

    private final AdminRepository adminRepository;
    private final WorkoutRepository workoutRepository;

    public DatabaseDataInitializer(WorkoutRepository workoutRepository, AdminRepository adminRepository) {
        this.workoutRepository = workoutRepository;
        this.adminRepository = adminRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {



//        // List of exercises
//        List<String> exercisesList = new ArrayList<>();
//        exercisesList.add("BURPEE");
//        exercisesList.add("BENCH PRESS");
//        exercisesList.add("DEADLIFT");
//        exercisesList.add("ROWING");
//
//        // Create workouts with the trainer's ID
//        workoutRepository.create(createNewWorkout("Full Body Workout", "A complete workout for all major muscle groups.", "/images/workout-card.jpg", exercisesList));
//        workoutRepository.create(createNewWorkout("Flexibility and Stretching", "Improves flexibility and range of motion.", "/images/workout-card.jpg", exercisesList));
//        workoutRepository.create(createNewWorkout("Back and Shoulders", "Strengthens the back and shoulder muscles.", "/images/workout-card.jpg", exercisesList));
//        workoutRepository.create(createNewWorkout("Leg Day", "Focuses on building strength in the lower body.", "/images/workout-card.jpg", exercisesList));
    }

//    private Workout createNewWorkout(String name, String description, String pictureURL, List<String> exercises) {
//        Workout workout = new Workout();
//        workout.setName(name);
//        workout.setDescription(description);
//        workout.setPictureURL(pictureURL);
//        workout.setExercises(exercises);
//        return workout;
//    }
}
