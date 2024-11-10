package fitness_app_be.fitness_app.exception_handling;

public class FileConversionException extends RuntimeException {

    // Constructor with both message and cause
    public FileConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only message
    public FileConversionException(String message) {
        super(message);
    }
}
