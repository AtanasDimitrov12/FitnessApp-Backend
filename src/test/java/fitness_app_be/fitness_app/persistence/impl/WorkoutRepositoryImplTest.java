package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutEntityMapper;
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

class WorkoutRepositoryImplTest {

    @Mock
    private JpaWorkoutRepository jpaWorkoutRepository;

    @Mock
    private WorkoutEntityMapper workoutMapper;

    @InjectMocks
    private WorkoutRepositoryImpl workoutRepositoryImpl;

    private Workout mockWorkout;
    private WorkoutEntity mockWorkoutEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<String> exercises = Arrays.asList("Push-ups", "Sit-ups", "Squats");
        mockWorkout = new Workout(1L, 1L, "Strength Training", "Builds strength", "./images/workout.jpg", Arrays.asList("Squats", "Deadlift"), null);
        mockWorkoutEntity = new WorkoutEntity(1L, null, "Strength Training", "Builds strength", "./images/workout.jpg", exercises, null);
    }

    @Test
    void exists() {
        when(jpaWorkoutRepository.existsById(1L)).thenReturn(true);

        boolean exists = workoutRepositoryImpl.exists(1L);

        assertTrue(exists, "Workout should exist with ID 1.");
        verify(jpaWorkoutRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaWorkoutRepository.findAll()).thenReturn(Arrays.asList(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        List<Workout> workouts = workoutRepositoryImpl.getAll();

        assertNotNull(workouts, "The list of workouts should not be null.");
        assertEquals(1, workouts.size(), "The number of workouts returned does not match.");
        assertEquals("Strength Training", workouts.get(0).getName(), "The workout name does not match.");

        verify(jpaWorkoutRepository, times(1)).findAll();
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void create() {
        when(workoutMapper.toEntity(mockWorkout)).thenReturn(mockWorkoutEntity);
        when(jpaWorkoutRepository.save(mockWorkoutEntity)).thenReturn(mockWorkoutEntity);
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        Workout createdWorkout = workoutRepositoryImpl.create(mockWorkout);

        assertNotNull(createdWorkout, "The created workout should not be null.");
        assertEquals("Strength Training", createdWorkout.getName(), "The workout name does not match.");

        verify(workoutMapper, times(1)).toEntity(mockWorkout);
        verify(jpaWorkoutRepository, times(1)).save(mockWorkoutEntity);
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void update() {
        when(workoutMapper.toEntity(mockWorkout)).thenReturn(mockWorkoutEntity);
        when(jpaWorkoutRepository.existsById(mockWorkout.getId())).thenReturn(true);
        when(jpaWorkoutRepository.save(mockWorkoutEntity)).thenReturn(mockWorkoutEntity);
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        Workout updatedWorkout = workoutRepositoryImpl.update(mockWorkout);

        assertNotNull(updatedWorkout, "The updated workout should not be null.");
        assertEquals("Strength Training", updatedWorkout.getName(), "The workout name does not match.");

        verify(workoutMapper, times(1)).toEntity(mockWorkout);
        verify(jpaWorkoutRepository, times(1)).save(mockWorkoutEntity);
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void delete() {
        workoutRepositoryImpl.delete(1L);

        verify(jpaWorkoutRepository, times(1)).deleteById(1L);
    }

    @Test
    void getWorkoutById() {
        when(jpaWorkoutRepository.findById(1L)).thenReturn(Optional.of(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        Optional<Workout> workout = workoutRepositoryImpl.getWorkoutById(1L);

        assertTrue(workout.isPresent(), "The workout should be present.");
        assertEquals("Strength Training", workout.get().getName(), "The workout name does not match.");

        verify(jpaWorkoutRepository, times(1)).findById(1L);
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void getWorkoutsByTrainer() {
        when(jpaWorkoutRepository.findByTrainerId(1L)).thenReturn(Arrays.asList(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        List<Workout> workouts = workoutRepositoryImpl.getWorkoutsByTrainer(1L);

        assertNotNull(workouts, "The list of workouts should not be null.");
        assertEquals(1, workouts.size(), "The number of workouts returned does not match.");
        assertEquals("Strength Training", workouts.get(0).getName(), "The workout name does not match.");

        verify(jpaWorkoutRepository, times(1)).findByTrainerId(1L);
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        when(jpaWorkoutRepository.findByNameContainingIgnoreCase("Strength")).thenReturn(Arrays.asList(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        List<Workout> workouts = workoutRepositoryImpl.findByNameContainingIgnoreCase("Strength");

        assertNotNull(workouts, "The list of workouts should not be null.");
        assertEquals(1, workouts.size(), "The number of workouts returned does not match.");
        assertEquals("Strength Training", workouts.get(0).getName(), "The workout name does not match.");

        verify(jpaWorkoutRepository, times(1)).findByNameContainingIgnoreCase("Strength");
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void findByExercises() {
        when(jpaWorkoutRepository.findByExercisesContaining("Squats")).thenReturn(Arrays.asList(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        List<Workout> workouts = workoutRepositoryImpl.findByExercises("Squats");

        assertNotNull(workouts, "The list of workouts should not be null.");
        assertEquals(1, workouts.size(), "The number of workouts returned does not match.");
        assertTrue(workouts.get(0).getExercises().contains("Squats"), "The exercise does not match.");

        verify(jpaWorkoutRepository, times(1)).findByExercisesContaining("Squats");
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }

    @Test
    void findByDescriptionContainingIgnoreCase() {
        when(jpaWorkoutRepository.findByDescriptionContainingIgnoreCase("Builds")).thenReturn(Arrays.asList(mockWorkoutEntity));
        when(workoutMapper.toDomain(mockWorkoutEntity)).thenReturn(mockWorkout);

        List<Workout> workouts = workoutRepositoryImpl.findByDescriptionContainingIgnoreCase("Builds");

        assertNotNull(workouts, "The list of workouts should not be null.");
        assertEquals(1, workouts.size(), "The number of workouts returned does not match.");
        assertEquals("Builds strength", workouts.get(0).getDescription(), "The workout description does not match.");

        verify(jpaWorkoutRepository, times(1)).findByDescriptionContainingIgnoreCase("Builds");
        verify(workoutMapper, times(1)).toDomain(mockWorkoutEntity);
    }
}
