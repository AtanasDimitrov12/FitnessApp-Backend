package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.ProgressNote;

import java.util.List;
import java.util.Optional;

public interface ProgressNoteRepository {
    boolean exists(long id);

    List<ProgressNote> getAll();

    ProgressNote create(ProgressNote progressNote);

    ProgressNote update(ProgressNote progressNote);

    void delete(long progressNoteId);

    Optional<ProgressNote> getProgressNoteById(long progressNoteId);

    List<ProgressNote> findByUserId(Long userId);
}
