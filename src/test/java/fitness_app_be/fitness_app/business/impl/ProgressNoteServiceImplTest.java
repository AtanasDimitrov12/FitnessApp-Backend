package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.exception_handling.ProgressNoteNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ProgressNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressNoteServiceImplTest {

    @Mock
    private ProgressNoteRepository progressNoteRepository;

    @InjectMocks
    private ProgressNoteServiceImpl progressNoteService;

    private ProgressNote progressNote;

    @BeforeEach
    void setUp() {
        progressNote = new ProgressNote(1L, 1L, 75.5, "Weekly progress", LocalDate.now());

    }

    @Test
    void getAllProgressNotes_ShouldReturnListOfProgressNotes() {
        when(progressNoteRepository.getAll()).thenReturn(List.of(progressNote));

        List<ProgressNote> notes = progressNoteService.getAllProgressNotes();

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(progressNote, notes.get(0));
        verify(progressNoteRepository, times(1)).getAll();
    }

    @Test
    void getProgressNoteById_ShouldReturnProgressNote_WhenNoteExists() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.of(progressNote));

        ProgressNote foundNote = progressNoteService.getProgressNoteById(1L);

        assertNotNull(foundNote);
        assertEquals(progressNote, foundNote);
        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
    }

    @Test
    void getProgressNoteById_ShouldThrowException_WhenNoteNotFound() {
        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.empty());

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteService.getProgressNoteById(1L));
        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
    }

    @Test
    void createProgressNote_ShouldReturnCreatedProgressNote() {
        when(progressNoteRepository.create(any(ProgressNote.class))).thenReturn(progressNote);

        ProgressNote createdNote = progressNoteService.createProgressNote(progressNote);

        assertNotNull(createdNote);
        assertEquals(progressNote, createdNote);
        verify(progressNoteRepository, times(1)).create(progressNote);
    }

    @Test
    void deleteProgressNote_ShouldDeleteProgressNote_WhenNoteExists() {
        when(progressNoteRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> progressNoteService.deleteProgressNote(1L));
        verify(progressNoteRepository, times(1)).exists(1L);
        verify(progressNoteRepository, times(1)).delete(1L);
    }

    @Test
    void deleteProgressNote_ShouldThrowException_WhenNoteNotFound() {
        when(progressNoteRepository.exists(1L)).thenReturn(false);

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteService.deleteProgressNote(1L));
        verify(progressNoteRepository, times(1)).exists(1L);
        verify(progressNoteRepository, never()).delete(1L);
    }

    @Test
    void updateProgressNote_ShouldReturnUpdatedProgressNote_WhenNoteExists() {
        ProgressNote updatedNote = new ProgressNote(1L, 1L, 76, "Updated progress", LocalDate.now());


        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.of(progressNote));
        when(progressNoteRepository.update(progressNote)).thenReturn(updatedNote);

        ProgressNote result = progressNoteService.updateProgressNote(updatedNote);

        assertNotNull(result);
        assertEquals("Updated progress", result.getNote());
        assertEquals(76.0, result.getWeight());
        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
        verify(progressNoteRepository, times(1)).update(progressNote);
    }

    @Test
    void updateProgressNote_ShouldThrowException_WhenNoteNotFound() {
        ProgressNote updatedNote = new ProgressNote(1L, 1L, 76, "Updated progress", LocalDate.now());


        when(progressNoteRepository.getProgressNoteById(1L)).thenReturn(Optional.empty());

        assertThrows(ProgressNoteNotFoundException.class, () -> progressNoteService.updateProgressNote(updatedNote));
        verify(progressNoteRepository, times(1)).getProgressNoteById(1L);
        verify(progressNoteRepository, never()).update(any(ProgressNote.class));
    }
}
