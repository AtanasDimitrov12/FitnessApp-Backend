package fitness_app_be.fitness_app.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrainerNotFoundException extends RuntimeException {

  public TrainerNotFoundException(Long id) {
    super("Trainer not found with ID: " + id);
  }

  public TrainerNotFoundException(String message) {
    super(message);
  }
}

