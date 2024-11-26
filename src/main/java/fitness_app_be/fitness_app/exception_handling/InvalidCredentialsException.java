package fitness_app_be.fitness_app.exception_handling;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {

        super("Invalid username or password");
    }
}

