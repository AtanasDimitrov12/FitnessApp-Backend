package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanRepositoryImplTest {

    @Mock
    private JpaWorkoutPlanRepository jpaWorkoutPlanRepository;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;

    @InjectMocks
    private WorkoutPlanRepositoryImpl workoutPlanRepository;

    private WorkoutPlan workoutPlan;
    private WorkoutPlanEntity workoutPlanEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        workoutPlan = new WorkoutPlan(1L, List.of(), List.of(),List.of(),List.of());
        userEntity = new UserEntity();
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
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(workoutPlanEntityMapper.toEntity(workoutPlan)).thenReturn(workoutPlanEntity);
        when(jpaWorkoutPlanRepository.save(workoutPlanEntity)).thenReturn(workoutPlanEntity);
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        WorkoutPlan result = workoutPlanRepository.create(workoutPlan);

        assertNotNull(result);
        assertEquals(workoutPlan, result);
        verify(jpaWorkoutPlanRepository, times(1)).save(workoutPlanEntity);
    }

    @Test
    void update_ShouldReturnUpdatedWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(userEntity));
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

        assertThrows(IllegalArgumentException.class, () -> workoutPlanRepository.update(workoutPlan));
        verify(jpaWorkoutPlanRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteWorkoutPlanById() {
        doNothing().when(jpaWorkoutPlanRepository).deleteById(1L);

        workoutPlanRepository.delete(1L);

        verify(jpaWorkoutPlanRepository, times(1)).deleteById(1L);
    }

    @Test
    void getWorkoutPlanById_ShouldReturnWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);
        when(jpaWorkoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        Optional<WorkoutPlan> result = workoutPlanRepository.getWorkoutPlanById(1L);

        assertTrue(result.isPresent());
        assertEquals(workoutPlan, result.get());
        verify(jpaWorkoutPlanRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutPlanById_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> workoutPlanRepository.getWorkoutPlanById(1L));
        verify(jpaWorkoutPlanRepository, never()).findById(anyLong());
    }

    @Test
    void getWorkoutPlanByUserId_ShouldReturnWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.existsByUsers_Id(1L)).thenReturn(true);
        when(jpaWorkoutPlanRepository.findByUsers_Id(1L)).thenReturn(Optional.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        Optional<WorkoutPlan> result = workoutPlanRepository.getWorkoutPlanByUserId(1L);

        assertTrue(result.isPresent());
        assertEquals(workoutPlan, result.get());
        verify(jpaWorkoutPlanRepository, times(1)).findByUsers_Id(1L);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsByUsers_Id(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> workoutPlanRepository.getWorkoutPlanByUserId(1L));
        verify(jpaWorkoutPlanRepository, never()).findByUsers_Id(anyLong());
    }
}
