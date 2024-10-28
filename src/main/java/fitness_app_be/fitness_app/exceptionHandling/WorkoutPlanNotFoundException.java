package fitness_app_be.fitness_app.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkoutPlanNotFoundException extends RuntimeException {

    public WorkoutPlanNotFoundException(Long id) {
        super("Workout plan with ID " + id + " not found");
    }

    public WorkoutPlanNotFoundException(String message) {
        super(message);
    }
}
