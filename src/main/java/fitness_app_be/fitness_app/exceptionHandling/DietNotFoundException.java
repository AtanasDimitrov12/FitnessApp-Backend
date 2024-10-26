package fitness_app_be.fitness_app.exceptionHandling;

public class DietNotFoundException extends RuntimeException {

    public DietNotFoundException(Long id) {
        super("Diet with ID " + id + " not found.");
    }

    public DietNotFoundException(String message) {
        super(message);
    }
}