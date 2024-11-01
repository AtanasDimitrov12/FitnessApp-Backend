package fitness_app_be.fitness_app.exception_handling;

public class CreationException extends RuntimeException {
  public CreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
