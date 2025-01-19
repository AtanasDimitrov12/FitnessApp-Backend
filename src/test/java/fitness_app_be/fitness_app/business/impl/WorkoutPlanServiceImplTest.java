package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.business.WorkoutStatusService;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutStatusService workoutStatusService;

    @InjectMocks
    private WorkoutPlanServiceImpl workoutPlanService; // FIX: Use InjectMocks to test the real service logic

    private WorkoutPlan workoutPlan;
    private Workout workout1;
    private Workout workout2;

    @BeforeEach
    void setUp() {
        workout1 = new Workout(2L, "Workout 1", "A great workout for overall fitness.", "PictureURL", null, null, null, null);
        workout2 = new Workout(3L, "Workout 2", "A great workout for overall fitness.", "PictureURL", null, null, null, null);
        List<Workout> workouts = new ArrayList<>(List.of(workout1, workout2));
        workoutPlan = new WorkoutPlan(1L, 1L, workouts);
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
        doNothing().when(workoutPlanRepository).delete(1L);

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
        when(workoutService.getWorkoutById(2L)).thenReturn(workout1);
        when(workoutService.getWorkoutById(3L)).thenReturn(workout2);
        when(workoutPlanRepository.update(workoutPlan)).thenReturn(workoutPlan);

        doNothing().when(workoutStatusService).deleteByWorkoutPlanId(1L);
        doNothing().when(workoutStatusService).saveAll(anyList());

        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(workoutPlan);

        assertNotNull(updatedWorkoutPlan);
        assertEquals(workoutPlan, updatedWorkoutPlan);

        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
        verify(workoutService, times(1)).getWorkoutById(2L);
        verify(workoutService, times(1)).getWorkoutById(3L);
        verify(workoutPlanRepository, times(1)).update(workoutPlan);
        verify(workoutStatusService, times(1)).deleteByWorkoutPlanId(1L);
        verify(workoutStatusService, times(1)).saveAll(anyList());
    }

    @Test
    void updateWorkoutPlan_ShouldThrowException_WhenWorkoutPlanNotFound() {
        when(workoutPlanRepository.getWorkoutPlanById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workoutPlanService.updateWorkoutPlan(workoutPlan));

        verify(workoutPlanRepository, times(1)).getWorkoutPlanById(1L);
        verify(workoutPlanRepository, never()).update(any(WorkoutPlan.class));
        verify(workoutService, never()).getWorkoutById(anyLong());
        verify(workoutStatusService, never()).deleteByWorkoutPlanId(anyLong());
        verify(workoutStatusService, never()).saveAll(anyList());
    }
}
