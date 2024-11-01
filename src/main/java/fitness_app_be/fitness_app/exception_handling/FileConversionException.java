package fitness_app_be.fitness_app.exception_handling;

public class FileConversionException extends RuntimeException {
    public FileConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
