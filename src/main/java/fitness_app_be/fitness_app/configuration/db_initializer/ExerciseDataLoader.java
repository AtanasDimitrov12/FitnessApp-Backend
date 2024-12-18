package fitness_app_be.fitness_app.configuration.db_initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExerciseDataLoader implements CommandLineRunner {

    private final ExerciseServiceDBInit exerciseServiceDBInit;

    public ExerciseDataLoader(ExerciseServiceDBInit exerciseServiceDBInit) {
        this.exerciseServiceDBInit = exerciseServiceDBInit;
    }

    @Override
    public void run(String... args) throws Exception {
        exerciseServiceDBInit.populateExercises();
    }
}

