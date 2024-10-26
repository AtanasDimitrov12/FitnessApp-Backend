package fitness_app_be.fitness_app.exceptionHandling;

public class MealNotFoundException extends RuntimeException {

    public MealNotFoundException(Long id) {
        super("Meal with ID " + id + " not found.");
    }

    public MealNotFoundException(String message) {
        super(message);
    }
}

