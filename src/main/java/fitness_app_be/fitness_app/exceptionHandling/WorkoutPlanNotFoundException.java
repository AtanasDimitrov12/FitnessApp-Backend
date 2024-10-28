package fitness_app_be.fitness_app.exceptionHandling;

public class WorkoutPlanNotFoundException extends RuntimeException {

    public WorkoutPlanNotFoundException(Long id) {
        super("Workout plan with ID " + id + " not found");
    }

    public WorkoutPlanNotFoundException(String message) {
        super(message);
    }
}
