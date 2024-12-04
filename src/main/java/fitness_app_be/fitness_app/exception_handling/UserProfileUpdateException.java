package fitness_app_be.fitness_app.exception_handling;

public class UserProfileUpdateException extends Exception {
    public UserProfileUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
