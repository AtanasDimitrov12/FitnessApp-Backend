package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exception_handling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;


    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private Workout workout;

    @BeforeEach
    void setUp() throws IOException {
        List<Exercise> exercises = new ArrayList<>();
        workout = new Workout(1L, "Test Workout", "Description", "http://example.com/image.jpg", exercises, null, null, null);

        // Use lenient() to prevent unnecessary stubbing exception
        lenient().when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://example.com/image.jpg");
        lenient().when(uploader.upload(any(File.class), anyMap())).thenReturn(uploadResult);
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
        when(workoutRepository.getWorkoutById(anyLong())).thenReturn(Optional.of(workout));

        Workout foundWorkout = workoutService.getWorkoutById(1L);

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
    void updateWorkout_ShouldReturnUpdatedWorkout_WhenWorkoutExists() throws IOException {
        // Arrange
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutRepository.update(workout)).thenReturn(workout);

        // Create a temporary file to use as the imageFile
        File tempFile = File.createTempFile("test-image", ".jpg");
        tempFile.deleteOnExit(); // Ensure the temp file is deleted after the test

        // Act
        Workout updatedWorkout = workoutService.updateWorkout(workout, tempFile);

        // Assert
        assertNotNull(updatedWorkout);
        assertEquals(workout, updatedWorkout);
        verify(workoutRepository, times(1)).getWorkoutById(1L);
        verify(workoutRepository, times(1)).update(workout);
    }

}
