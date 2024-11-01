package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserDietPreferenceNotFoundException extends RuntimeException {

    public UserDietPreferenceNotFoundException(Long id) {
        super("User diet preference not found with ID: " + id);
    }

    public UserDietPreferenceNotFoundException(String message) {
        super(message);
    }
}
