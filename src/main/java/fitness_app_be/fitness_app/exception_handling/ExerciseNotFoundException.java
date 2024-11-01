package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExerciseNotFoundException extends RuntimeException {

    public ExerciseNotFoundException(Long id) {
        super("Exercise not found with ID: " + id);
    }

    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
