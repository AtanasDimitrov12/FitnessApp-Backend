package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.ProgressNoteService;
import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.exception_handling.ProgressNoteNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ProgressNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressNoteServiceImpl implements ProgressNoteService {

    private final ProgressNoteRepository progressNoteRepository;

    @Override
    public List<ProgressNote> getAllProgressNotes() {
        return progressNoteRepository.getAll();
    }

    @Override
    public ProgressNote getProgressNoteById(Long id) {
        return progressNoteRepository.getProgressNoteById(id)
                .orElseThrow(() -> new ProgressNoteNotFoundException(id));
    }

    @Override
    public ProgressNote createProgressNote(ProgressNote progressNote) {
        return progressNoteRepository.create(progressNote);
    }

    @Override
    public void deleteProgressNote(Long id) {
        if (!progressNoteRepository.exists(id)) {
            throw new ProgressNoteNotFoundException(id);
        }
        progressNoteRepository.delete(id);
    }

    @Override
    public ProgressNote updateProgressNote(ProgressNote progressNote) {
        ProgressNote existingNote = progressNoteRepository.getProgressNoteById(progressNote.getId())
                .orElseThrow(() -> new ProgressNoteNotFoundException("ProgressNote with ID " + progressNote.getId() + " not found"));

        existingNote.setWeight(progressNote.getWeight());
        existingNote.setNote(progressNote.getNote());
        existingNote.setDate(progressNote.getDate());

        return progressNoteRepository.update(existingNote);
    }
}
