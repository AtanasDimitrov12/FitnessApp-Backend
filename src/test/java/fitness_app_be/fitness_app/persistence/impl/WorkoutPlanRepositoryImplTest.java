package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
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

    @InjectMocks
    private WorkoutPlanRepositoryImpl workoutPlanRepository;
    @InjectMocks
    private UserRepository userRepository;
    @InjectMocks
    private UserEntityMapper userEntityMapper;

    private WorkoutPlan workoutPlan;
    private WorkoutPlanEntity workoutPlanEntity;

    @BeforeEach
    void setUp() {
        List<Workout> workouts = new ArrayList<>();

        workoutPlan = new WorkoutPlan(1L, 1L, workouts);
        List<WorkoutEntity> workoutsEntity = new ArrayList<>();
        Optional<User> user = userRepository.getUserById(1L);
        UserEntity userEntity = userEntityMapper.toEntity(user.orElseThrow(() -> new UserNotFoundException("User not found with ID: 101")));
        workoutPlanEntity = new WorkoutPlanEntity(1L, userEntity, workoutsEntity);

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

        List<WorkoutPlan> workoutPlans = workoutPlanRepository.getAll();

        assertNotNull(workoutPlans);
        assertEquals(1, workoutPlans.size());
        assertEquals(workoutPlan, workoutPlans.get(0));
        verify(jpaWorkoutPlanRepository, times(1)).findAll();
        verify(workoutPlanEntityMapper, times(1)).toDomain(workoutPlanEntity);
    }

    @Test
    void create_ShouldReturnCreatedWorkoutPlan() {
        Optional<User> user = userRepository.getUserById(1L);
        UserEntity userEntity = userEntityMapper.toEntity(user.orElseThrow(() -> new UserNotFoundException("User not found with ID: 101")));

        when(workoutPlanEntityMapper.toEntity(workoutPlan, userEntity)).thenReturn(workoutPlanEntity);
        when(jpaWorkoutPlanRepository.save(workoutPlanEntity)).thenReturn(workoutPlanEntity);
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        WorkoutPlan createdWorkoutPlan = workoutPlanRepository.create(workoutPlan);

        assertNotNull(createdWorkoutPlan);
        assertEquals(workoutPlan, createdWorkoutPlan);
        verify(jpaWorkoutPlanRepository, times(1)).save(workoutPlanEntity);
        verify(workoutPlanEntityMapper, times(1)).toEntity(workoutPlan, userEntity);
        verify(workoutPlanEntityMapper, times(1)).toDomain(workoutPlanEntity);
    }

    @Test
    void update_ShouldReturnUpdatedWorkoutPlan_WhenExists() {
        Optional<User> user = userRepository.getUserById(1L);
        UserEntity userEntity = userEntityMapper.toEntity(user.orElseThrow(() -> new UserNotFoundException("User not found with ID: 101")));
        when(workoutPlanEntityMapper.toEntity(workoutPlan, userEntity)).thenReturn(workoutPlanEntity);
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(true);
        when(jpaWorkoutPlanRepository.save(workoutPlanEntity)).thenReturn(workoutPlanEntity);
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        WorkoutPlan updatedWorkoutPlan = workoutPlanRepository.update(workoutPlan);

        assertNotNull(updatedWorkoutPlan);
        assertEquals(workoutPlan, updatedWorkoutPlan);
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

        Optional<WorkoutPlan> foundWorkoutPlan = workoutPlanRepository.getWorkoutPlanById(1L);

        assertTrue(foundWorkoutPlan.isPresent());
        assertEquals(workoutPlan, foundWorkoutPlan.get());
        verify(jpaWorkoutPlanRepository, times(1)).findById(1L);
        verify(workoutPlanEntityMapper, times(1)).toDomain(workoutPlanEntity);
    }

    @Test
    void getWorkoutPlanById_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> workoutPlanRepository.getWorkoutPlanById(1L));
        verify(jpaWorkoutPlanRepository, never()).findById(anyLong());
    }

    @Test
    void getWorkoutPlanByUserId_ShouldReturnWorkoutPlan_WhenExists() {
        when(jpaWorkoutPlanRepository.existsByUserId(2L)).thenReturn(true);
        when(jpaWorkoutPlanRepository.findByUserId(2L)).thenReturn(Optional.of(workoutPlanEntity));
        when(workoutPlanEntityMapper.toDomain(workoutPlanEntity)).thenReturn(workoutPlan);

        Optional<WorkoutPlan> foundWorkoutPlan = workoutPlanRepository.getWorkoutPlanByUserId(2L);

        assertTrue(foundWorkoutPlan.isPresent());
        assertEquals(workoutPlan, foundWorkoutPlan.get());
        verify(jpaWorkoutPlanRepository, times(1)).findByUserId(2L);
        verify(workoutPlanEntityMapper, times(1)).toDomain(workoutPlanEntity);
    }

    @Test
    void getWorkoutPlanByUserId_ShouldThrowException_WhenWorkoutPlanDoesNotExist() {
        when(jpaWorkoutPlanRepository.existsByUserId(2L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> workoutPlanRepository.getWorkoutPlanByUserId(2L));
        verify(jpaWorkoutPlanRepository, never()).findByUserId(anyLong());
    }
}
