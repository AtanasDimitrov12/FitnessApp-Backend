package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.mapper.ProgressNoteEntityMapper;
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

class ProgressNoteRepositoryImplTest {

    @Mock
    private JpaProgressNoteRepository jpaProgressNoteRepository;

    @Mock
    private ProgressNoteEntityMapper progressNoteEntityMapper;

    @InjectMocks
    private ProgressNoteRepositoryImpl progressNoteRepositoryImpl;

    private ProgressNote mockProgressNote;
    private ProgressNoteEntity mockProgressNoteEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProgressNote = new ProgressNote(1L, 1L, 75.0, "Workout progress", "./images/note.jpg");
        mockProgressNoteEntity = new ProgressNoteEntity(1L, null, 75.0, "Workout progress", "./images/note.jpg");
    }

    @Test
    void exists() {
        when(jpaProgressNoteRepository.existsById(1L)).thenReturn(true);

        boolean exists = progressNoteRepositoryImpl.exists(1L);

        assertTrue(exists, "ProgressNote should exist with ID 1.");
        verify(jpaProgressNoteRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaProgressNoteRepository.findAll()).thenReturn(Arrays.asList(mockProgressNoteEntity));
        when(progressNoteEntityMapper.toDomain(mockProgressNoteEntity)).thenReturn(mockProgressNote);

        List<ProgressNote> progressNotes = progressNoteRepositoryImpl.getAll();

        assertNotNull(progressNotes, "The list of progress notes should not be null.");
        assertEquals(1, progressNotes.size(), "The number of progress notes returned does not match.");
        assertEquals("Workout progress", progressNotes.get(0).getNote(), "The progress note text does not match.");

        verify(jpaProgressNoteRepository, times(1)).findAll();
        verify(progressNoteEntityMapper, times(1)).toDomain(mockProgressNoteEntity);
    }

    @Test
    void create() {
        when(progressNoteEntityMapper.toEntity(mockProgressNote)).thenReturn(mockProgressNoteEntity);
        when(jpaProgressNoteRepository.save(mockProgressNoteEntity)).thenReturn(mockProgressNoteEntity);
        when(progressNoteEntityMapper.toDomain(mockProgressNoteEntity)).thenReturn(mockProgressNote);

        ProgressNote createdProgressNote = progressNoteRepositoryImpl.create(mockProgressNote);

        assertNotNull(createdProgressNote, "The created progress note should not be null.");
        assertEquals("Workout progress", createdProgressNote.getNote(), "The progress note text does not match.");

        verify(progressNoteEntityMapper, times(1)).toEntity(mockProgressNote);
        verify(jpaProgressNoteRepository, times(1)).save(mockProgressNoteEntity);
        verify(progressNoteEntityMapper, times(1)).toDomain(mockProgressNoteEntity);
    }

    @Test
    void update() {
        when(progressNoteEntityMapper.toEntity(mockProgressNote)).thenReturn(mockProgressNoteEntity);
        when(jpaProgressNoteRepository.save(mockProgressNoteEntity)).thenReturn(mockProgressNoteEntity);
        when(progressNoteEntityMapper.toDomain(mockProgressNoteEntity)).thenReturn(mockProgressNote);

        mockProgressNote.setWeight(78.0);

        ProgressNote updatedProgressNote = progressNoteRepositoryImpl.update(mockProgressNote);

        assertNotNull(updatedProgressNote, "The updated progress note should not be null.");
        assertEquals(78.0, updatedProgressNote.getWeight(), "The progress note weight did not update correctly.");

        verify(progressNoteEntityMapper, times(1)).toEntity(mockProgressNote);
        verify(jpaProgressNoteRepository, times(1)).save(mockProgressNoteEntity);
        verify(progressNoteEntityMapper, times(1)).toDomain(mockProgressNoteEntity);
    }

    @Test
    void delete() {
        progressNoteRepositoryImpl.delete(1L);

        verify(jpaProgressNoteRepository, times(1)).deleteById(1L);
    }

    @Test
    void getProgressNoteById() {
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.of(mockProgressNoteEntity));
        when(progressNoteEntityMapper.toDomain(mockProgressNoteEntity)).thenReturn(mockProgressNote);

        Optional<ProgressNote> progressNote = progressNoteRepositoryImpl.getProgressNoteById(1L);

        assertTrue(progressNote.isPresent(), "The progress note should be present.");
        assertEquals("Workout progress", progressNote.get().getNote(), "The progress note text does not match.");

        verify(jpaProgressNoteRepository, times(1)).findById(1L);
        verify(progressNoteEntityMapper, times(1)).toDomain(mockProgressNoteEntity);
    }

    @Test
    void findByUserId() {
        Long userId = 1L;
        when(jpaProgressNoteRepository.findByUserId(userId)).thenReturn(Arrays.asList(mockProgressNoteEntity));
        when(progressNoteEntityMapper.toDomain(mockProgressNoteEntity)).thenReturn(mockProgressNote);

        List<ProgressNote> progressNotes = progressNoteRepositoryImpl.findByUserId(userId);

        assertNotNull(progressNotes, "The list of progress notes should not be null.");
        assertEquals(1, progressNotes.size(), "The number of progress notes returned does not match.");
        assertEquals(1L, progressNotes.get(0).getUserId(), "The user ID of the progress note does not match.");

        verify(jpaProgressNoteRepository, times(1)).findByUserId(userId);
        verify(progressNoteEntityMapper, times(1)).toDomain(mockProgressNoteEntity);
    }
}
