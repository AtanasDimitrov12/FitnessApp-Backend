package fitness_app_be.fitness_app.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserWorkoutPreferenceNotFoundException extends RuntimeException {

    public UserWorkoutPreferenceNotFoundException(Long id) {
        super("User workout preference not found with ID: " + id);
    }

    public UserWorkoutPreferenceNotFoundException(String message) {
        super(message);
    }
}
