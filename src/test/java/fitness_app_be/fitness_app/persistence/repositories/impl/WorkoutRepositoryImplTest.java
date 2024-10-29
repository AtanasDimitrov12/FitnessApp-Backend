package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutEntityMapper;
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
class WorkoutRepositoryImplTest {

    @Mock
    private JpaWorkoutRepository jpaWorkoutRepository;

    @Mock
    private WorkoutEntityMapper workoutMapper;

    @InjectMocks
    private WorkoutRepositoryImpl workoutRepository;

    private Workout workout;
    private WorkoutEntity workoutEntity;

    @BeforeEach
    void setUp() {

        workout = new Workout(1L, "Test Workout", "Description", "http://example.com/image.jpg", List.of(), List.of());

        workoutEntity = new WorkoutEntity(1L, "Test Workout", "Description", "http://example.com/image.jpg",List.of(), List.of());

    }

    @Test
    void exists_ShouldReturnTrue_WhenWorkoutExists() {
        when(jpaWorkoutRepository.existsById(1L)).thenReturn(true);

        assertTrue(workoutRepository.exists(1L));
        verify(jpaWorkoutRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenWorkoutDoesNotExist() {
        when(jpaWorkoutRepository.existsById(1L)).thenReturn(false);

        assertFalse(workoutRepository.exists(1L));
        verify(jpaWorkoutRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfWorkouts() {
        when(jpaWorkoutRepository.findAll()).thenReturn(List.of(workoutEntity));
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        List<Workout> workouts = workoutRepository.getAll();

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(jpaWorkoutRepository, times(1)).findAll();
        verify(workoutMapper, times(1)).toDomain(workoutEntity);
    }

    @Test
    void create_ShouldReturnCreatedWorkout() {
        when(workoutMapper.toEntity(workout)).thenReturn(workoutEntity);
        when(jpaWorkoutRepository.save(workoutEntity)).thenReturn(workoutEntity);
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        Workout createdWorkout = workoutRepository.create(workout);

        assertNotNull(createdWorkout);
        assertEquals(workout, createdWorkout);
        verify(jpaWorkoutRepository, times(1)).save(workoutEntity);
    }

    @Test
    void update_ShouldReturnUpdatedWorkout_WhenWorkoutExists() {
        when(workoutMapper.toEntity(workout)).thenReturn(workoutEntity);
        when(jpaWorkoutRepository.existsById(1L)).thenReturn(true);
        when(jpaWorkoutRepository.save(workoutEntity)).thenReturn(workoutEntity);
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        Workout updatedWorkout = workoutRepository.update(workout);

        assertNotNull(updatedWorkout);
        assertEquals(workout, updatedWorkout);
        verify(jpaWorkoutRepository, times(1)).save(workoutEntity);
    }

    @Test
    void update_ShouldThrowException_WhenWorkoutDoesNotExist() {
        when(jpaWorkoutRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> workoutRepository.update(workout));
        verify(jpaWorkoutRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteWorkoutById() {
        doNothing().when(jpaWorkoutRepository).deleteById(1L);

        workoutRepository.delete(1L);

        verify(jpaWorkoutRepository, times(1)).deleteById(1L);
    }

    @Test
    void getWorkoutById_ShouldReturnWorkout_WhenExists() {
        when(jpaWorkoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        Optional<Workout> foundWorkout = workoutRepository.getWorkoutById(1L);

        assertTrue(foundWorkout.isPresent());
        assertEquals(workout, foundWorkout.get());
        verify(jpaWorkoutRepository, times(1)).findById(1L);
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnListOfWorkouts() {
        when(jpaWorkoutRepository.findByNameContainingIgnoreCase("test")).thenReturn(List.of(workoutEntity));
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        List<Workout> workouts = workoutRepository.findByNameContainingIgnoreCase("test");

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(jpaWorkoutRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    @Test
    void findByExercises_ShouldReturnListOfWorkouts() {
        when(jpaWorkoutRepository.findByExercises_NameContaining("exercise")).thenReturn(List.of(workoutEntity));
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        List<Workout> workouts = workoutRepository.findByExercises("exercise");

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(jpaWorkoutRepository, times(1)).findByExercises_NameContaining("exercise");
    }

    @Test
    void findByDescriptionContainingIgnoreCase_ShouldReturnListOfWorkouts() {
        when(jpaWorkoutRepository.findByDescriptionContainingIgnoreCase("description")).thenReturn(List.of(workoutEntity));
        when(workoutMapper.toDomain(workoutEntity)).thenReturn(workout);

        List<Workout> workouts = workoutRepository.findByDescriptionContainingIgnoreCase("description");

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(jpaWorkoutRepository, times(1)).findByDescriptionContainingIgnoreCase("description");
    }
}
