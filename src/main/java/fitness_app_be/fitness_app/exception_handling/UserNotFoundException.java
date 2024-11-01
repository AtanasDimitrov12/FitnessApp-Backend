package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {

        super("User not found with ID: " + id);
    }


    public UserNotFoundException(String message) {

        super(message);
    }
}