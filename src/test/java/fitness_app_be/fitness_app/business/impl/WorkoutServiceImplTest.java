package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exception_handling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ExerciseRepository;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private Workout workout;
    private Exercise exercise;
    private File imageFile;

    @BeforeEach
    void setUp() {

        exercise = new Exercise(1L, "Push-up", 3, 3, MuscleGroup.BACK);
        workout = new Workout(1L, "Full Body Workout", "A great workout for overall fitness.", "PictureURL", List.of(exercise), null, null, null);

        imageFile = mock(File.class);
    }

    @Test
    void getAllWorkouts_ShouldReturnListOfWorkouts() {
        when(workoutRepository.getAll()).thenReturn(List.of(workout));

        List<Workout> result = workoutService.getAllWorkouts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Full Body Workout", result.get(0).getName());

        verify(workoutRepository, times(1)).getAll();
    }

    @Test
    void getWorkoutById_ShouldReturnWorkout_WhenWorkoutExists() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(workout));

        Workout result = workoutService.getWorkoutById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }

    @Test
    void getWorkoutById_ShouldThrowException_WhenWorkoutNotFound() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.getWorkoutById(1L));

        verify(workoutRepository, times(1)).getWorkoutById(1L);
    }

    @Test
    void createWorkout_ShouldReturnCreatedWorkout() throws Exception {
        when(imageFile.exists()).thenReturn(true);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(File.class), any(Map.class)))
                .thenReturn(Map.of("url", "http://cloudinary.com/image.jpg"));

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(workoutRepository.create(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout, imageFile);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/image.jpg", result.getPictureURL());
        verify(workoutRepository, times(1)).create(any(Workout.class));
    }


    @Test
    void createWorkout_ShouldThrowException_WhenExerciseNotFound() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.createWorkout(workout, imageFile));

        verify(exerciseRepository, times(1)).findById(1L);
        verify(workoutRepository, never()).create(any(Workout.class)); // Ensure workout is NOT created
    }

    @Test
    void deleteWorkout_ShouldDeleteWorkout_WhenWorkoutExists() {
        when(workoutRepository.exists(1L)).thenReturn(true);

        workoutService.deleteWorkout(1L);

        verify(workoutRepository, times(1)).delete(1L);
    }

    @Test
    void deleteWorkout_ShouldThrowException_WhenWorkoutDoesNotExist() {
        when(workoutRepository.exists(1L)).thenReturn(false);

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(1L));

        verify(workoutRepository, never()).delete(anyLong()); // Ensure delete was NOT called
    }

    @Test
    void searchWorkoutsByPartialUsername_ShouldReturnMatchingWorkouts() {
        when(workoutRepository.findByNameContainingIgnoreCase("Full")).thenReturn(List.of(workout));

        List<Workout> result = workoutService.searchWorkoutsByPartialUsername("Full");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Full Body Workout", result.get(0).getName());

        verify(workoutRepository, times(1)).findByNameContainingIgnoreCase("Full");
    }

    @Test
    void updateWorkout_ShouldReturnUpdatedWorkout() throws Exception {
        when(imageFile.exists()).thenReturn(true);
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(File.class), any(Map.class)))
                .thenReturn(Map.of("url", "http://cloudinary.com/new-image.jpg"));

        when(workoutRepository.update(any(Workout.class))).thenReturn(workout);

        Workout updatedWorkout = new Workout(1L, "Updated Workout", "Updated description", "PictureURL", List.of(exercise), null, null, null);

        Workout result = workoutService.updateWorkout(updatedWorkout, imageFile);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/new-image.jpg", result.getPictureURL());
        verify(workoutRepository, times(1)).update(any(Workout.class));
    }


    @Test
    void updateWorkout_ShouldThrowException_WhenWorkoutNotFound() {
        when(workoutRepository.getWorkoutById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.updateWorkout(workout, imageFile));

        verify(workoutRepository, times(1)).getWorkoutById(1L);
        verify(workoutRepository, never()).update(any(Workout.class)); // Ensure update is NOT called
    }
}
