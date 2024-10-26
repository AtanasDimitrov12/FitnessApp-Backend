package fitness_app_be.fitness_app.exceptionHandling;

public class ProgressNoteNotFoundException extends RuntimeException {

    public ProgressNoteNotFoundException(Long id) {
        super("Progress Note with ID " + id + " not found.");
    }

    public ProgressNoteNotFoundException(String message) {
        super(message);
    }
}
