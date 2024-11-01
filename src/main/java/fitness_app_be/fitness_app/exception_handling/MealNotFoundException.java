package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MealNotFoundException extends RuntimeException {

    public MealNotFoundException(Long id) {
        super("Meal with ID " + id + " not found.");
    }

    public MealNotFoundException(String message) {
        super(message);
    }
}

