package fitness_app_be.fitness_app.configuration.db_initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WorkoutDataLoader implements CommandLineRunner {

    private final WorkoutServiceDBInit workoutServiceDBInit;

    public WorkoutDataLoader(WorkoutServiceDBInit workoutServiceDBInit) {
        this.workoutServiceDBInit = workoutServiceDBInit;
    }

    @Override
    public void run(String... args) throws Exception {
        workoutServiceDBInit.populateWorkouts();
    }
}
