package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.impl.WorkoutServiceImpl;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exceptionHandling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private File imageFile;

    @InjectMocks
    @Spy
    private WorkoutServiceImpl workoutServiceImpl;

    private Workout mockWorkout;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockWorkout = new Workout(1L, "Strength Training", "Workout focused on building strength", "./images/user.jpg", Arrays.asList());
    }

    @Test
    void getAllWorkouts() {
        List<Workout> workouts = Arrays.asList(mockWorkout);
        when(workoutRepository.getAll()).thenReturn(workouts);

        List<Workout> workoutList = workoutServiceImpl.getAllWorkouts();

        assertNotNull(workoutList, "The returned list of workouts should not be null.");
        assertEquals(1, workoutList.size(), "The size of the workout list does not match.");
        assertEquals("Strength Training", workoutList.get(0).getName(), "The workout name does not match.");

        verify(workoutRepository, times(1)).getAll();
    }

    @Test
    void getWorkoutById() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(mockWorkout));

        Workout workout = workoutServiceImpl.getWorkoutById(1L);

        assertNotNull(workout, "The returned workout should not be null.");
        assertEquals("Strength Training", workout.getName(), "The workout name does not match.");

        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }

    @Test
    void getWorkoutById_WorkoutNotFound() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutServiceImpl.getWorkoutById(1L));

        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }

    @Test
    void createWorkout() throws IOException {
        when(workoutRepository.create(mockWorkout)).thenReturn(mockWorkout);
        doReturn("http://mock-url.com/image.jpg").when(workoutServiceImpl).uploadImageToCloudinary(imageFile);

        Workout createdWorkout = workoutServiceImpl.createWorkout(mockWorkout, imageFile);

        assertNotNull(createdWorkout, "The created workout should not be null.");
        assertEquals("Strength Training", createdWorkout.getName(), "The workout name does not match.");
        assertEquals("http://mock-url.com/image.jpg", createdWorkout.getPictureURL(), "The image URL does not match.");

        verify(workoutRepository, times(1)).create(mockWorkout);
        verify(workoutServiceImpl, times(1)).uploadImageToCloudinary(imageFile);
    }

    @Test
    void deleteWorkout() {
        when(workoutRepository.exists(1L)).thenReturn(true);

        workoutServiceImpl.deleteWorkout(1L);

        verify(workoutRepository, times(1)).delete(1L);
    }

    @Test
    void deleteWorkout_WorkoutNotFound() {
        when(workoutRepository.exists(1L)).thenReturn(false);

        assertThrows(WorkoutNotFoundException.class, () -> workoutServiceImpl.deleteWorkout(1L));

        verify(workoutRepository, times(1)).exists(1L);
        verify(workoutRepository, never()).delete(1L);
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
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(mockWorkout));
        when(workoutRepository.update(mockWorkout)).thenReturn(mockWorkout);

        mockWorkout.setName("Updated Workout Name");

        Workout updatedWorkout = workoutServiceImpl.updateWorkout(mockWorkout);

        assertNotNull(updatedWorkout, "The updated workout should not be null.");
        assertEquals("Updated Workout Name", updatedWorkout.getName(), "The workout name did not update correctly.");

        verify(workoutRepository, times(1)).getWorkoutById(1L);
        verify(workoutRepository, times(1)).update(mockWorkout);
    }
}
