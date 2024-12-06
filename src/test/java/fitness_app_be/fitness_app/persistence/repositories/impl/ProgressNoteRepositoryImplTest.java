package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.mapper.ProgressNoteEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressNoteRepositoryImplTest {

    @Mock
    private JpaProgressNoteRepository jpaProgressNoteRepository;

    @Mock
    private ProgressNoteEntityMapper progressNoteEntityMapper;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProgressNoteRepositoryImpl progressNoteRepository;

    private ProgressNote progressNote;
    private ProgressNoteEntity progressNoteEntity;
    private UserEntity userEntity;


    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(101L);
        userEntity.setNotes(new ArrayList<>());

        progressNote = new ProgressNote(1L, 101L, 85, "Weekly progress", LocalDate.now());
        progressNoteEntity = new ProgressNoteEntity(1L, userEntity, 85, "Weekly progress", LocalDate.now());
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
        when(progressNoteEntityMapper.toDomain(progressNoteEntity)).thenReturn(progressNote);

        List<ProgressNote> notes = progressNoteRepository.getAll();

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(progressNote, notes.get(0));
        verify(jpaProgressNoteRepository, times(1)).findAll();
        verify(progressNoteEntityMapper, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void create_ShouldReturnCreatedProgressNote() {
        // Arrange
        // Mock the User domain object
        User user = new User(101L, "username", "email@example.com", "password", null, null, null, null, null, Role.USER, null, null, null, true);
        when(userEntityMapper.toDomain(userEntity)).thenReturn(user); // Map UserEntity to User

        // Mock the behavior of userRepository and entity mappers
        when(userRepository.findEntityById(101L)).thenReturn(userEntity); // Return UserEntity when searched
        when(progressNoteEntityMapper.toEntity(progressNote)).thenReturn(progressNoteEntity); // Map ProgressNote to ProgressNoteEntity
        when(jpaProgressNoteRepository.save(progressNoteEntity)).thenReturn(progressNoteEntity); // Save ProgressNoteEntity
        when(progressNoteEntityMapper.toDomain(progressNoteEntity)).thenReturn(progressNote); // Map ProgressNoteEntity to ProgressNote

        // Act
        ProgressNote createdNote = progressNoteRepository.create(progressNote);

        // Assert
        assertNotNull(createdNote, "The created note should not be null");
        assertEquals(progressNote, createdNote, "The created note should match the input");

        // Verify interactions
        verify(userRepository, times(1)).findEntityById(101L); // Verify fetching user
        verify(progressNoteEntityMapper, times(1)).toEntity(progressNote); // Verify mapping to entity
        verify(jpaProgressNoteRepository, times(1)).save(progressNoteEntity); // Verify saving entity
        verify(progressNoteEntityMapper, times(1)).toDomain(progressNoteEntity); // Verify mapping back to domain
        verify(userRepository, times(1)).update(user); // Verify updating User domain object
    }


    @Test
    void create_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findEntityById(101L)).thenReturn(null);

        Exception exception = assertThrows(UserNotFoundException.class, () -> progressNoteRepository.create(progressNote));

        assertEquals("User not found with ID: 101", exception.getMessage());
        verify(userRepository, times(1)).findEntityById(101L);
        verifyNoInteractions(progressNoteEntityMapper, jpaProgressNoteRepository);
    }



    @Test
    void update_ShouldReturnUpdatedProgressNote() {
        when(progressNoteEntityMapper.toEntity(progressNote)).thenReturn(progressNoteEntity);
        when(jpaProgressNoteRepository.save(progressNoteEntity)).thenReturn(progressNoteEntity);
        when(progressNoteEntityMapper.toDomain(progressNoteEntity)).thenReturn(progressNote);

        ProgressNote updatedNote = progressNoteRepository.update(progressNote);

        assertNotNull(updatedNote);
        assertEquals(progressNote, updatedNote);
        verify(jpaProgressNoteRepository, times(1)).save(progressNoteEntity);
        verify(progressNoteEntityMapper, times(1)).toEntity(progressNote);
        verify(progressNoteEntityMapper, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void delete_ShouldDeleteProgressNoteById() {
        // Arrange
        ProgressNoteEntity mockNote = new ProgressNoteEntity(1L, null, 85, "Sample Note", LocalDate.now());
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.of(mockNote)); // Simulate finding the note
        doNothing().when(jpaProgressNoteRepository).delete(mockNote); // Simulate deletion behavior

        // Act
        progressNoteRepository.delete(1L);

        // Assert
        verify(jpaProgressNoteRepository, times(1)).findById(1L); // Verify findById was called
        verify(jpaProgressNoteRepository, times(1)).delete(mockNote); // Verify delete was called with the right entity
    }


    @Test
    void delete_ShouldThrowExceptionWhenNoteNotFound() {
        // Arrange
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.empty()); // Simulate not finding the note

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> progressNoteRepository.delete(1L));
        assertEquals("Note not found", exception.getMessage(), "Exception message should match");

        // Verify interactions
        verify(jpaProgressNoteRepository, times(1)).findById(1L); // Verify findById was called
        verify(jpaProgressNoteRepository, never()).delete(any()); // Verify delete was not called
    }




    @Test
    void getProgressNoteById_ShouldReturnProgressNote_WhenExists() {
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.of(progressNoteEntity));
        when(progressNoteEntityMapper.toDomain(progressNoteEntity)).thenReturn(progressNote);

        Optional<ProgressNote> foundNote = progressNoteRepository.getProgressNoteById(1L);

        assertTrue(foundNote.isPresent());
        assertEquals(progressNote, foundNote.get());
        verify(jpaProgressNoteRepository, times(1)).findById(1L);
        verify(progressNoteEntityMapper, times(1)).toDomain(progressNoteEntity);
    }

    @Test
    void getProgressNoteById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaProgressNoteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ProgressNote> foundNote = progressNoteRepository.getProgressNoteById(1L);

        assertTrue(foundNote.isEmpty());
        verify(jpaProgressNoteRepository, times(1)).findById(1L);
        verify(progressNoteEntityMapper, never()).toDomain(any());
    }

    @Test
    void findByUserId_ShouldReturnListOfProgressNotes() {
        when(jpaProgressNoteRepository.findByUserId(101L)).thenReturn(List.of(progressNoteEntity));
        when(progressNoteEntityMapper.toDomain(progressNoteEntity)).thenReturn(progressNote);

        List<ProgressNote> notes = progressNoteRepository.findByUserId(101L);

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(progressNote, notes.get(0));
        verify(jpaProgressNoteRepository, times(1)).findByUserId(101L);
        verify(progressNoteEntityMapper, times(1)).toDomain(progressNoteEntity);
    }
}
