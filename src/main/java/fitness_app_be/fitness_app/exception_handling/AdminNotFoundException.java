package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdminNotFoundException extends RuntimeException {

  public AdminNotFoundException(Long id) {
    super("Admin not found with ID: " + id);
  }

  public AdminNotFoundException(String message) {
    super(message);
  }
}

