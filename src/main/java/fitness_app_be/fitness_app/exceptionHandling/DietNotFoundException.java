package fitness_app_be.fitness_app.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DietNotFoundException extends RuntimeException {

    public DietNotFoundException(Long id) {
        super("Diet with ID " + id + " not found.");
    }

    public DietNotFoundException(String message) {
        super(message);
    }
}