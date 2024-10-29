package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exceptionHandling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private Cloudinary cloudinary;


    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private Workout workout;

    @BeforeEach
    void setUp() {
        List<Exercise> exercises = new ArrayList<>();
        List<WorkoutPlan> plans = new ArrayList<>();
        workout = new Workout(1L, "Test Workout", "Description", "http://example.com/image.jpg", exercises, plans);

    }

    @Test
    void getAllWorkouts_ShouldReturnListOfWorkouts() {
        when(workoutRepository.getAll()).thenReturn(List.of(workout));

        List<Workout> workouts = workoutService.getAllWorkouts();

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(workoutRepository, times(1)).getAll();
    }

    @Test
    void getWorkoutById_ShouldReturnWorkout_WhenWorkoutExists() {
        // Arrange: Stub the repository's response to return an Optional with the expected workout
        when(workoutRepository.getWorkoutById(anyLong())).thenReturn(Optional.of(workout));

        // Act: Call the service method to get the workout by ID
        Workout foundWorkout = workoutService.getWorkoutById(1L);

        // Assert: Verify the result and interaction with the mock repository
        assertNotNull(foundWorkout);
        assertEquals(workout, foundWorkout);
        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }


    @Test
    void getWorkoutById_ShouldThrowException_WhenWorkoutNotFound() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.getWorkoutById(1L));
        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }




    @Test
    void deleteWorkout_ShouldDeleteWorkout_WhenWorkoutExists() {
        when(workoutRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> workoutService.deleteWorkout(1L));
        verify(workoutRepository, times(1)).exists(1L);
        verify(workoutRepository, times(1)).delete(1L);
    }

    @Test
    void deleteWorkout_ShouldThrowException_WhenWorkoutNotFound() {
        when(workoutRepository.exists(1L)).thenReturn(false);

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(1L));
        verify(workoutRepository, times(1)).exists(1L);
        verify(workoutRepository, never()).delete(1L);
    }

    @Test
    void searchWorkoutsByPartialUsername_ShouldReturnMatchingWorkouts() {
        when(workoutRepository.findByNameContainingIgnoreCase("test")).thenReturn(List.of(workout));

        List<Workout> workouts = workoutService.searchWorkoutsByPartialUsername("test");

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout, workouts.get(0));
        verify(workoutRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    @Test
    void updateWorkout_ShouldReturnUpdatedWorkout_WhenWorkoutExists() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutRepository.update(workout)).thenReturn(workout);

        Workout updatedWorkout = workoutService.updateWorkout(workout);

        assertNotNull(updatedWorkout);
        assertEquals(workout, updatedWorkout);
        verify(workoutRepository, times(1)).getWorkoutById(1L);
        verify(workoutRepository, times(1)).update(workout);
    }



}
