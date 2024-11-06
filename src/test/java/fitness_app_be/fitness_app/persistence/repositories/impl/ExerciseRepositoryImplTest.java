package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaExerciseRepository;
import fitness_app_be.fitness_app.persistence.mapper.ExerciseEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseRepositoryImplTest {

    @Mock
    private JpaExerciseRepository jpaExerciseRepository;

    @Mock
    private ExerciseEntityMapper exerciseEntityMapper;

    @InjectMocks
    private ExerciseRepositoryImpl exerciseRepository;

    private Exercise exercise;
    private ExerciseEntity exerciseEntity;

    @BeforeEach
    void setUp() {
        exercise = new Exercise(1L, "Push Up", 3, 12, "Back",  List.of());
        exerciseEntity = new ExerciseEntity(1L, "Push Up", 3, 12, "Back", List.of());
    }

    @Test
    void exists_ShouldReturnTrue_WhenExerciseExists() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(true);

        assertTrue(exerciseRepository.exists(1L));
        verify(jpaExerciseRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenExerciseDoesNotExist() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(false);

        assertFalse(exerciseRepository.exists(1L));
        verify(jpaExerciseRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfExercises() {
        when(jpaExerciseRepository.findAll()).thenReturn(List.of(exerciseEntity));
        when(exerciseEntityMapper.toDomain(exerciseEntity)).thenReturn(exercise);

        List<Exercise> exercises = exerciseRepository.getAll();

        assertNotNull(exercises);
        assertEquals(1, exercises.size());
        assertEquals(exercise, exercises.get(0));
        verify(jpaExerciseRepository, times(1)).findAll();
        verify(exerciseEntityMapper, times(1)).toDomain(exerciseEntity);
    }

    @Test
    void create_ShouldReturnCreatedExercise() {
        when(exerciseEntityMapper.toEntity(exercise)).thenReturn(exerciseEntity);
        when(jpaExerciseRepository.save(exerciseEntity)).thenReturn(exerciseEntity);
        when(exerciseEntityMapper.toDomain(exerciseEntity)).thenReturn(exercise);

        Exercise createdExercise = exerciseRepository.create(exercise);

        assertNotNull(createdExercise);
        assertEquals(exercise, createdExercise);
        verify(jpaExerciseRepository, times(1)).save(exerciseEntity);
        verify(exerciseEntityMapper, times(1)).toEntity(exercise);
        verify(exerciseEntityMapper, times(1)).toDomain(exerciseEntity);
    }

    @Test
    void update_ShouldReturnUpdatedExercise_WhenExerciseExists() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(true);
        when(exerciseEntityMapper.toEntity(exercise)).thenReturn(exerciseEntity);
        when(jpaExerciseRepository.save(exerciseEntity)).thenReturn(exerciseEntity);
        when(exerciseEntityMapper.toDomain(exerciseEntity)).thenReturn(exercise);

        Exercise updatedExercise = exerciseRepository.update(exercise);

        assertNotNull(updatedExercise);
        assertEquals(exercise, updatedExercise);
        verify(jpaExerciseRepository, times(1)).existsById(1L);
        verify(jpaExerciseRepository, times(1)).save(exerciseEntity);
        verify(exerciseEntityMapper, times(1)).toEntity(exercise);
        verify(exerciseEntityMapper, times(1)).toDomain(exerciseEntity);
    }

    @Test
    void update_ShouldThrowException_WhenExerciseDoesNotExist() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> exerciseRepository.update(exercise));
        verify(jpaExerciseRepository, times(1)).existsById(1L);
        verify(jpaExerciseRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteExercise_WhenExerciseExists() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jpaExerciseRepository).deleteById(1L);

        exerciseRepository.delete(1L);

        verify(jpaExerciseRepository, times(1)).existsById(1L);
        verify(jpaExerciseRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenExerciseDoesNotExist() {
        when(jpaExerciseRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> exerciseRepository.delete(1L));
        verify(jpaExerciseRepository, times(1)).existsById(1L);
        verify(jpaExerciseRepository, never()).deleteById(anyLong());
    }

    @Test
    void getExerciseById_ShouldReturnExercise_WhenExists() {
        when(jpaExerciseRepository.findById(1L)).thenReturn(Optional.of(exerciseEntity));
        when(exerciseEntityMapper.toDomain(exerciseEntity)).thenReturn(exercise);

        Optional<Exercise> foundExercise = exerciseRepository.getExerciseById(1L);

        assertTrue(foundExercise.isPresent());
        assertEquals(exercise, foundExercise.get());
        verify(jpaExerciseRepository, times(1)).findById(1L);
        verify(exerciseEntityMapper, times(1)).toDomain(exerciseEntity);
    }

    @Test
    void getExerciseById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaExerciseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Exercise> foundExercise = exerciseRepository.getExerciseById(1L);

        assertFalse(foundExercise.isPresent());
        verify(jpaExerciseRepository, times(1)).findById(1L);
        verify(exerciseEntityMapper, never()).toDomain(any());
    }

    @Test
    void findByName_ShouldReturnExercise_WhenExists() {
        when(jpaExerciseRepository.findByName("Push Up")).thenReturn(Optional.of(exerciseEntity));
        when(exerciseEntityMapper.toDomain(exerciseEntity)).thenReturn(exercise);

        Optional<Exercise> foundExercise = exerciseRepository.findByName("Push Up");

        assertTrue(foundExercise.isPresent());
        assertEquals(exercise, foundExercise.get());
        verify(jpaExerciseRepository, times(1)).findByName("Push Up");
        verify(exerciseEntityMapper, times(1)).toDomain(exerciseEntity);
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaExerciseRepository.findByName("Nonexistent Exercise")).thenReturn(Optional.empty());

        Optional<Exercise> foundExercise = exerciseRepository.findByName("Nonexistent Exercise");

        assertFalse(foundExercise.isPresent());
        verify(jpaExerciseRepository, times(1)).findByName("Nonexistent Exercise");
        verify(exerciseEntityMapper, never()).toDomain(any());
    }
}
