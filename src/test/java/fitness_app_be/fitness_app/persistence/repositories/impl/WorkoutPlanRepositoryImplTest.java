package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaWorkoutRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import jakarta.persistence.EntityManager;
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
class WorkoutPlanRepositoryImplTest {

    @Mock
    private JpaWorkoutPlanRepository jpaWorkoutPlanRepository;

    @Mock
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaWorkoutRepository jpaWorkoutRepository;


    @InjectMocks
    private WorkoutPlanRepositoryImpl workoutPlanRepository;

    private WorkoutPlan workoutPlan;
    private WorkoutPlanEntity workoutPlanEntity;

    @BeforeEach
    void setUp() {
        workoutPlan = new WorkoutPlan(1L, 1L, List.of());
        workoutPlanEntity = new WorkoutPlanEntity();
    }

    @Test
    void exists_ShouldReturnTrue_WhenWorkoutPlanExists() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);

        assertTrue(workoutPlanRepository.exists(1L));
        verify(jpaWorkoutPlanRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(false);

        assertFalse(workoutPlanRepository.exists(1L));
        verify(jpaWorkoutPlanRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfWorkoutPlans() {
        when(jpaWorkoutPlanRepository.findAll()).thenReturn(List.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        List<WorkoutPlan> result = workoutPlanRepository.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(workoutPlan, result.get(0));
        verify(jpaWorkoutPlanRepository, times(1)).findAll();
    }

    @Test
    void create_ShouldReturnCreatedWorkoutPlan() {
        // Initialize the workouts list in WorkoutPlanEntity
        workoutPlanEntity.setWorkouts(new ArrayList<>()); // Ensure workouts is not null

        // Mock the mapper and repository behavior
        when(workoutPlanEntityMapper.toEntity(workoutPlan)).thenReturn(workoutPlanEntity);
        when(entityManager.merge(workoutPlanEntity)).thenReturn(workoutPlanEntity);
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        // Call the method under test
        WorkoutPlan result = workoutPlanRepository.create(workoutPlan);

        // Assert the result
        assertNotNull(result);
        assertEquals(workoutPlan, result);

        // Verify the interactions
        verify(entityManager, times(1)).merge(workoutPlanEntity);
        verify(workoutPlanEntityMapper, times(1)).toDomain(workoutPlanEntity);
    }


    @Test
    void update_ShouldReturnUpdatedWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);
        when(workoutPlanEntityMapper.toEntity(workoutPlan)).thenReturn(workoutPlanEntity);
        when(jpaWorkoutPlanRepository.save(workoutPlanEntity)).thenReturn(workoutPlanEntity);
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        WorkoutPlan result = workoutPlanRepository.update(workoutPlan);

        assertNotNull(result);
        assertEquals(workoutPlan, result);
        verify(jpaWorkoutPlanRepository, times(1)).save(workoutPlanEntity);
    }

    @Test
    void update_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(false);

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanRepository.update(workoutPlan));
        verify(jpaWorkoutPlanRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteWorkoutPlanById_WhenExists() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jpaWorkoutPlanRepository).deleteById(1L);

        assertDoesNotThrow(() -> workoutPlanRepository.delete(1L));
        verify(jpaWorkoutPlanRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(false);

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanRepository.delete(1L));
        verify(jpaWorkoutPlanRepository, never()).deleteById(anyLong());
    }

    @Test
    void getWorkoutPlanById_ShouldReturnWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        Optional<WorkoutPlan> result = workoutPlanRepository.getWorkoutPlanById(1L);

        assertTrue(result.isPresent());
        assertEquals(workoutPlan, result.get());
        verify(jpaWorkoutPlanRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutPlanById_ShouldReturnEmptyOptional_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<WorkoutPlan> result = workoutPlanRepository.getWorkoutPlanById(1L);

        assertTrue(result.isEmpty());
        verify(jpaWorkoutPlanRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldReturnWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.findByUserId(1L)).thenReturn(Optional.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        Optional<WorkoutPlan> result = workoutPlanRepository.getWorkoutPlanByUserId(1L);

        assertTrue(result.isPresent());
        assertEquals(workoutPlan, result.get());
        verify(jpaWorkoutPlanRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFoundException.class, () -> workoutPlanRepository.getWorkoutPlanByUserId(1L));
        verify(jpaWorkoutPlanRepository, times(1)).findByUserId(1L);
    }
}
