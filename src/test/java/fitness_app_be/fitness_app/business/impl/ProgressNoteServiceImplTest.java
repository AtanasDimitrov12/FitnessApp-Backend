package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.exceptionHandling.ProgressNoteNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ProgressNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressNoteServiceImplTest {

    @Mock
    private ProgressNoteRepository progressNoteRepository;

    @InjectMocks
    private ProgressNoteServiceImpl progressNoteServiceImpl;

    private ProgressNote mockProgressNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProgressNote = new ProgressNote(1L, 1L, 75.5, "Weekly progress", "./images/progress.jpg");
    }

    @Test
    void getAllProgressNotes() {
        List<ProgressNote> notes = Arrays.asList(mockProgressNote);
        when(progressNoteRepository.getAll()).thenReturn(notes);

        List<ProgressNote> result = progressNoteServiceImpl.getAllProgressNotes();

        assertNotNull(result, "The list of progress notes should not be null.");
        assertEquals(1, result.size(), "The size of the progress note list does not match.");
        assertEquals("Weekly progress", result.get(0).getNote(), "The progress note description does not match.");

        verify(progressNoteRepository, times(1)).getAll();
    }

    @Test
    void getProgressNoteById() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.of(mockProgressNote));

        ProgressNote note = progressNoteServiceImpl.getProgressNoteById(1L);

        assertNotNull(note, "The progress note should not be null.");
        assertEquals("Weekly progress", note.getNote(), "The progress note description does not match.");

        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
    }

    @Test
    void getProgressNoteById_ProgressNoteNotFound() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.empty());

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteServiceImpl.getProgressNoteById(1L), "Expected ProgressNoteNotFoundException");

        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
    }

    @Test
    void createProgressNote() {
        when(progressNoteRepository.create(mockProgressNote)).thenReturn(mockProgressNote);

        ProgressNote createdNote = progressNoteServiceImpl.createProgressNote(mockProgressNote);

        assertNotNull(createdNote, "The created progress note should not be null.");
        assertEquals("Weekly progress", createdNote.getNote(), "The progress note description does not match.");

        verify(progressNoteRepository, times(1)).create(mockProgressNote);
    }

    @Test
    void deleteProgressNote() {
        when(progressNoteRepository.exists(1L)).thenReturn(true);

        progressNoteServiceImpl.deleteProgressNote(1L);

        verify(progressNoteRepository, times(1)).delete(1L);
    }

    @Test
    void deleteProgressNote_ProgressNoteNotFound() {
        when(progressNoteRepository.exists(1L)).thenReturn(false);

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteServiceImpl.deleteProgressNote(1L), "Expected ProgressNoteNotFoundException");

        verify(progressNoteRepository, times(1)).exists(1L);
        verify(progressNoteRepository, never()).delete(1L);
    }

    @Test
    void updateProgressNote() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.of(mockProgressNote));
        when(progressNoteRepository.update(mockProgressNote)).thenReturn(mockProgressNote);

        mockProgressNote.setWeight(80.0);
        mockProgressNote.setNote("Updated progress");

        ProgressNote updatedNote = progressNoteServiceImpl.updateProgressNote(mockProgressNote);

        assertNotNull(updatedNote, "The updated progress note should not be null.");
        assertEquals("Updated progress", updatedNote.getNote(), "The progress note description was not updated correctly.");
        assertEquals(80.0, updatedNote.getWeight(), "The weight value did not update correctly.");

        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
        verify(progressNoteRepository, times(1)).update(mockProgressNote);
    }

    @Test
    void updateProgressNote_ProgressNoteNotFound() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.empty());

        mockProgressNote.setWeight(80.0);
        mockProgressNote.setNote("Updated progress");

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteServiceImpl.updateProgressNote(mockProgressNote), "Expected ProgressNoteNotFoundException");

        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
        verify(progressNoteRepository, never()).update(mockProgressNote);
    }
}
