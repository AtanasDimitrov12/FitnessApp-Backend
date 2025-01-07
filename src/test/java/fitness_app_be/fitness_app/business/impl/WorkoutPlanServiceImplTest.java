package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
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
class WorkoutPlanServiceImplTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @InjectMocks
    private WorkoutPlanServiceImpl workoutPlanService;

    private WorkoutPlan workoutPlan;

    @BeforeEach
    void setUp() {
        List<Workout> workouts = new ArrayList<>();
        workoutPlan = new WorkoutPlan(1L,1L, workouts);

    }

    @Test
    void getAllWorkoutPlans_ShouldReturnListOfWorkoutPlans() {
        when(workoutPlanRepository.getAll()).thenReturn(List.of(workoutPlan));

        List<WorkoutPlan> workoutPlans = workoutPlanService.getAllWorkoutPlans();

        assertNotNull(workoutPlans);
        assertEquals(1, workoutPlans.size());
        assertEquals(workoutPlan, workoutPlans.get(0));
        verify(workoutPlanRepository, times(1)).getAll();
    }

    @Test
    void getWorkoutPlanById_ShouldReturnWorkoutPlan_WhenWorkoutPlanExists() {
        when(workoutPlanRepository.getWorkoutPlanById(1L)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlan foundWorkoutPlan = workoutPlanService.getWorkoutPlanById(1L);

        assertNotNull(foundWorkoutPlan);
        assertEquals(workoutPlan, foundWorkoutPlan);
        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
    }

    @Test
    void getWorkoutPlanById_ShouldThrowException_WhenWorkoutPlanNotFound() {
        when(workoutPlanRepository.getWorkoutPlanById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanService.getWorkoutPlanById(1L));
        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldReturnWorkoutPlan_WhenWorkoutPlanExists() {
        when(workoutPlanRepository.getWorkoutPlanByUserId(100L)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlan foundWorkoutPlan = workoutPlanService.getWorkoutPlanByUserId(100L);

        assertNotNull(foundWorkoutPlan);
        assertEquals(workoutPlan, foundWorkoutPlan);
        verify(workoutPlanRepository, times(1)).getWorkoutPlanByUserId(100L);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldThrowException_WhenWorkoutPlanNotFound() {
        when(workoutPlanRepository.getWorkoutPlanByUserId(100L)).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanService.getWorkoutPlanByUserId(100L));
        verify(workoutPlanRepository, times(1)).getWorkoutPlanByUserId(100L);
    }

    @Test
    void createWorkoutPlan_ShouldReturnCreatedWorkoutPlan() {
        when(workoutPlanRepository.create(workoutPlan)).thenReturn(workoutPlan);

        WorkoutPlan createdWorkoutPlan = workoutPlanService.createWorkoutPlan(workoutPlan);

        assertNotNull(createdWorkoutPlan);
        assertEquals(workoutPlan, createdWorkoutPlan);
        verify(workoutPlanRepository, times(1)).create(workoutPlan);
    }

    @Test
    void deleteWorkoutPlan_ShouldDeleteWorkoutPlan_WhenWorkoutPlanExists() {
        when(workoutPlanRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> workoutPlanService.deleteWorkoutPlan(1L));
        verify(workoutPlanRepository, times(1)).exists(1L);
        verify(workoutPlanRepository, times(1)).delete(1L);
    }

    @Test
    void deleteWorkoutPlan_ShouldThrowException_WhenWorkoutPlanNotFound() {
        when(workoutPlanRepository.exists(1L)).thenReturn(false);

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanService.deleteWorkoutPlan(1L));
        verify(workoutPlanRepository, times(1)).exists(1L);
        verify(workoutPlanRepository, never()).delete(1L);
    }

    @Test
    void updateWorkoutPlan_ShouldReturnUpdatedWorkoutPlan_WhenWorkoutPlanExists() {
        when(workoutPlanRepository.getWorkoutPlanById(1L)).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanRepository.update(workoutPlan)).thenReturn(workoutPlan);

        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(workoutPlan);

        assertNotNull(updatedWorkoutPlan);
        assertEquals(workoutPlan, updatedWorkoutPlan);
        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
        verify(workoutPlanRepository, times(1)).update(workoutPlan);
    }

    @Test
    void updateWorkoutPlan_ShouldThrowException_WhenWorkoutPlanNotFound() {
        when(workoutPlanRepository.getWorkoutPlanById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanService.updateWorkoutPlan(workoutPlan));
        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
        verify(workoutPlanRepository, never()).update(any(WorkoutPlan.class));
    }
}
