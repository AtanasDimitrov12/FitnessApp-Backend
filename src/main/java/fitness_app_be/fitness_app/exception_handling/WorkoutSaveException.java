package fitness_app_be.fitness_app.exception_handling;


public class WorkoutSaveException extends RuntimeException {
    public WorkoutSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

