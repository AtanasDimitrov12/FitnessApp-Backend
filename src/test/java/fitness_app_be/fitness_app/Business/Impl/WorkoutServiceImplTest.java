package fitness_app_be.fitness_app.Business.Impl;

import fitness_app_be.fitness_app.Domain.Workout;
import fitness_app_be.fitness_app.ExceptionHandling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.Persistence.WorkoutRepository;
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

class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutServiceImpl workoutServiceImpl;

    private Workout mockWorkout;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockWorkout = new Workout(1L, "Strength Training", "Workout focused on building strength", Arrays.asList());
    }

    @Test
    void getAllWorkouts() {
        List<Workout> workouts = Arrays.asList(mockWorkout);
        when(workoutRepository.findAll()).thenReturn(workouts);

        List<Workout> workoutList = workoutServiceImpl.getAllWorkouts();

        assertNotNull(workoutList, "The returned list of workouts should not be null.");
        assertEquals(1, workoutList.size(), "The size of the workout list does not match.");
        assertEquals("Strength Training", workoutList.get(0).getName(), "The workout name does not match.");

        verify(workoutRepository, times(1)).findAll();
    }

    @Test
    void getWorkoutById() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(mockWorkout));

        Workout workout = workoutServiceImpl.getWorkoutById(1L);

        assertNotNull(workout, "The returned workout should not be null.");
        assertEquals("Strength Training", workout.getName(), "The workout name does not match.");

        verify(workoutRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutById_WorkoutNotFound() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutServiceImpl.getWorkoutById(1L));

        verify(workoutRepository, times(1)).findById(1L);
    }

    @Test
    void createWorkout() {
        when(workoutRepository.save(mockWorkout)).thenReturn(mockWorkout);

        Workout createdWorkout = workoutServiceImpl.createWorkout(mockWorkout);

        assertNotNull(createdWorkout, "The created workout should not be null.");
        assertEquals("Strength Training", createdWorkout.getName());

        verify(workoutRepository, times(1)).save(mockWorkout);
    }

    @Test
    void deleteWorkout() {
        workoutServiceImpl.deleteWorkout(1L);

        verify(workoutRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchWorkoutsByPartialUsername() {
        String partialUsername = "Strength";
        List<Workout> workouts = Arrays.asList(mockWorkout);

        when(workoutRepository.findByNameContainingIgnoreCase(partialUsername)).thenReturn(workouts);

        List<Workout> result = workoutServiceImpl.searchWorkoutsByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "The number of workouts returned does not match.");
        assertEquals("Strength Training", result.get(0).getName(), "The workout name does not match.");

        verify(workoutRepository, times(1)).findByNameContainingIgnoreCase(partialUsername);
    }

    @Test
    void updateWorkout() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(mockWorkout));
        when(workoutRepository.save(mockWorkout)).thenReturn(mockWorkout);

        mockWorkout.setName("Updated Workout Name");

        Workout updatedWorkout = workoutServiceImpl.updateWorkout(mockWorkout);

        assertNotNull(updatedWorkout, "The updated workout should not be null.");
        assertEquals("Updated Workout Name", updatedWorkout.getName(), "The workout name did not update correctly.");

        verify(workoutRepository, times(1)).findById(1L);
        verify(workoutRepository, times(1)).save(mockWorkout);
    }
}
