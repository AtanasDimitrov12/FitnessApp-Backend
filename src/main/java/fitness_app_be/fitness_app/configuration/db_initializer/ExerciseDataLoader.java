package fitness_app_be.fitness_app.configuration.db_initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ExerciseDataLoader implements CommandLineRunner {

    private final ExerciseServiceDBInit exerciseServiceDBInit;

    public ExerciseDataLoader(ExerciseServiceDBInit exerciseServiceDBInit) {
        this.exerciseServiceDBInit = exerciseServiceDBInit;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start populate exercise data");
        exerciseServiceDBInit.populateExercises();
    }
}

