package fitness_app_be.fitness_app.exception_handling;

public class CustomFileUploadException extends Exception {
    public CustomFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
