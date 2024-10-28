package fitness_app_be.fitness_app.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkoutNotFoundException extends RuntimeException {

    public WorkoutNotFoundException(Long id) {
        super("Workout with ID " + id + " not found.");
    }

    public WorkoutNotFoundException(String message) {
        super(message);
    }
}

