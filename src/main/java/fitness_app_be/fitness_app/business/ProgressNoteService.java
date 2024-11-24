package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.ProgressNote;

import java.util.List;

public interface ProgressNoteService {
    List<ProgressNote> getAllProgressNotes();
    ProgressNote getProgressNoteById(Long id);
    List<ProgressNote> getProgressNotesByUserId(Long userId);
    ProgressNote createProgressNote(ProgressNote progressNote);
    void deleteProgressNote(Long id);
    ProgressNote updateProgressNote(ProgressNote progressNote);
}
