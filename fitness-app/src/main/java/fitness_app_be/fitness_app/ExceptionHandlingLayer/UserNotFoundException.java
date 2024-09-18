package fitness_app_be.fitness_app.ExceptionHandlingLayer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {

        super("User not found with ID: " + id);
    }

    // Constructor for email not found or other custom messages
    public UserNotFoundException(String message) {
        super(message);
    }
}