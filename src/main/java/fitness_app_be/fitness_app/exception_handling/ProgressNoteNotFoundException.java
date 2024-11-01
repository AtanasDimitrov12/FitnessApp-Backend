package fitness_app_be.fitness_app.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProgressNoteNotFoundException extends RuntimeException {

    public ProgressNoteNotFoundException(Long id) {
        super("Progress Note with ID " + id + " not found.");
    }

    public ProgressNoteNotFoundException(String message) {
        super(message);
    }
}
