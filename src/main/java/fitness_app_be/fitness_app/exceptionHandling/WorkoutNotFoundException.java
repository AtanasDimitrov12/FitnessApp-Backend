package fitness_app_be.fitness_app.exceptionHandling;

public class WorkoutNotFoundException extends RuntimeException {

    public WorkoutNotFoundException(Long id) {
        super("Workout with ID " + id + " not found.");
    }

    public WorkoutNotFoundException(String message) {
        super(message);
    }
}

