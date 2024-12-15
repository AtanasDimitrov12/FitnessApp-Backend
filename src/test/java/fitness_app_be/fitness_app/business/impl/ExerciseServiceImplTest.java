package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exception_handling.ExerciseNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        List<Workout> workouts = new ArrayList<>();
        exercise = new Exercise(1L, "Push-up", 3, 15, "Back");
    }

    @Test
    void getAllExercises_ReturnsListOfExercises() {
        when(exerciseRepository.getAll()).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getAllExercises();

        assertEquals(1, result.size());
        assertEquals(exercise, result.get(0));
        verify(exerciseRepository, times(1)).getAll();
    }

    @Test
    void getExerciseById_ExerciseExists_ReturnsExercise() {
        when(exerciseRepository.getExerciseById(1L)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getExerciseById(1L);

        assertEquals(exercise, result);
        verify(exerciseRepository, times(1)).getExerciseById(1L);
    }

    @Test
    void getExerciseById_ExerciseDoesNotExist_ThrowsException() {
        when(exerciseRepository.getExerciseById(1L)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseService.getExerciseById(1L));
        verify(exerciseRepository, times(1)).getExerciseById(1L);
    }

    @Test
    void createExercise_ReturnsCreatedExercise() {
        when(exerciseRepository.create(exercise)).thenReturn(exercise);

        Exercise result = exerciseService.createExercise(exercise);

        assertEquals(exercise, result);
        verify(exerciseRepository, times(1)).create(exercise);
    }

    @Test
    void deleteExercise_ExecutesWithoutException() {
        doNothing().when(exerciseRepository).delete(1L);

        assertDoesNotThrow(() -> exerciseService.deleteExercise(1L));
        verify(exerciseRepository, times(1)).delete(1L);
    }

    @Test
    void updateExercise_ReturnsUpdatedExercise() {
        when(exerciseRepository.update(exercise)).thenReturn(exercise);

        Exercise result = exerciseService.updateExercise(exercise);

        assertEquals(exercise, result);
        verify(exerciseRepository, times(1)).update(exercise);
    }
}
