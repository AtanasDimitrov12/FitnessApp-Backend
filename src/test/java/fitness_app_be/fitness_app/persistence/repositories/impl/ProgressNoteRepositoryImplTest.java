package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.*;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.mapper.ProgressNoteEntityMapper;
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
class ProgressNoteRepositoryImplTest {

    @Mock
    private JpaProgressNoteRepository jpaProgressNoteRepository;

    @Mock
    private ProgressNoteEntityMapper progressNoteEntityMapperImpl;

    @InjectMocks
    private ProgressNoteRepositoryImpl progressNoteRepository;

    private ProgressNote progressNote;
    private ProgressNoteEntity progressNoteEntity;

    @BeforeEach
    void setUp() {
        progressNote = new ProgressNote(1L, 101L, 85, "Weekly progress", LocalDate.now());
        progressNoteEntity = new ProgressNoteEntity(1L, null, 85, "Weekly progress", LocalDate.now()); // Simplified UserEntity for brevity
    }


    @Test
    void exists_ShouldReturnTrue_WhenProgressNoteExists() {
        when(jpaProgressNoteRepository.existsById(1L)).thenReturn(true);

        assertTrue(progressNoteRepository.exists(1L));
        verify(jpaProgressNoteRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenProgressNoteDoesNotExist() {
        when(jpaProgressNoteRepository.existsById(1L)).thenReturn(false);

        assertFalse(progressNoteRepository.exists(1L));
        verify(jpaProgressNoteRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfProgressNotes() {
        when(jpaProgressNoteRepository.findAll()).thenReturn(List.of(progressNoteEntity));
        when(progressNoteEntityMapperImpl.toDomain(progressNoteEntity)).thenReturn(progressNote);

        List<ProgressNote> notes = progressNoteRepository.getAll();

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(progressNote, notes.get(0));
        verify(jpaProgressNoteRepository, times(1)).findAll();
        verify(progressNoteEntityMapperImpl, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void create_ShouldReturnCreatedProgressNote() {
        when(progressNoteEntityMapperImpl.toEntity(progressNote)).thenReturn(progressNoteEntity);
        when(jpaProgressNoteRepository.save(progressNoteEntity)).thenReturn(progressNoteEntity);
        when(progressNoteEntityMapperImpl.toDomain(progressNoteEntity)).thenReturn(progressNote);

        ProgressNote createdNote = progressNoteRepository.create(progressNote);

        assertNotNull(createdNote);
        assertEquals(progressNote, createdNote);
        verify(jpaProgressNoteRepository, times(1)).save(progressNoteEntity);
        verify(progressNoteEntityMapperImpl, times(1)).toEntity(progressNote);
        verify(progressNoteEntityMapperImpl, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void update_ShouldReturnUpdatedProgressNote() {
        when(progressNoteEntityMapperImpl.toEntity(progressNote)).thenReturn(progressNoteEntity);
        when(jpaProgressNoteRepository.save(progressNoteEntity)).thenReturn(progressNoteEntity);
        when(progressNoteEntityMapperImpl.toDomain(progressNoteEntity)).thenReturn(progressNote);

        ProgressNote updatedNote = progressNoteRepository.update(progressNote);

        assertNotNull(updatedNote);
        assertEquals(progressNote, updatedNote);
        verify(jpaProgressNoteRepository, times(1)).save(progressNoteEntity);
        verify(progressNoteEntityMapperImpl, times(1)).toEntity(progressNote);
        verify(progressNoteEntityMapperImpl, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void delete_ShouldDeleteProgressNoteById() {
        doNothing().when(jpaProgressNoteRepository).deleteById(1L);

        progressNoteRepository.delete(1L);

        verify(jpaProgressNoteRepository, times(1)).deleteById(1L);
    }

    @Test
    void getProgressNoteById_ShouldReturnProgressNote_WhenExists() {
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.of(progressNoteEntity));
        when(progressNoteEntityMapperImpl.toDomain(progressNoteEntity)).thenReturn(progressNote);

        Optional<ProgressNote> foundNote = progressNoteRepository.getProgressNoteById(1L);

        assertTrue(foundNote.isPresent());
        assertEquals(progressNote, foundNote.get());
        verify(jpaProgressNoteRepository, times(1)).findById(1L);
        verify(progressNoteEntityMapperImpl, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void getProgressNoteById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ProgressNote> foundNote = progressNoteRepository.getProgressNoteById(1L);

        assertTrue(foundNote.isEmpty());
        verify(jpaProgressNoteRepository, times(1)).findById(1L);
        verify(progressNoteEntityMapperImpl, never()).toDomain(any());
    }

    @Test
    void findByUserId_ShouldReturnListOfProgressNotes() {
        when(jpaProgressNoteRepository.findByUserId(101L)).thenReturn(List.of(progressNoteEntity));
        when(progressNoteEntityMapperImpl.toDomain(progressNoteEntity)).thenReturn(progressNote);

        List<ProgressNote> notes = progressNoteRepository.findByUserId(101L);

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(progressNote, notes.get(0));
        verify(jpaProgressNoteRepository, times(1)).findByUserId(101L);
        verify(progressNoteEntityMapperImpl, times(1)).toDomain(progressNoteEntity);
    }
}
