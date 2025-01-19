package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanGenerator;
import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.UserWorkoutPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserWorkoutPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWorkoutPreferenceServiceImplTest {

    @Mock
    private UserWorkoutPreferenceRepository userWorkoutPreferenceRepository;

    @Mock
    private WorkoutPlanGenerator workoutPlanGenerator;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private UserService userService;

    @Mock
    private WorkoutPlanService workoutPlanService;


    @InjectMocks
    private UserWorkoutPreferenceServiceImpl userWorkoutPreferenceService;

    private UserWorkoutPreference userWorkoutPreference;
    private User user;
    private WorkoutPlan workoutPlan;
    private WorkoutPlan updatedWorkoutPlan;

    @BeforeEach
    void setUp() {
        userWorkoutPreference = new UserWorkoutPreference(1L, 1L, null, null, null, 4);
        user = new User(1L, "John Doe", "john@example.com", "password", null, userWorkoutPreference, "pictureURL", LocalDateTime.now(), LocalDateTime.now(),  Role.ADMIN,workoutPlan, null, null, true);
        workoutPlan = new WorkoutPlan();
        updatedWorkoutPlan = new WorkoutPlan();
    }

    @Test
    void getUserWorkoutPreferenceByUserId_UserExists_ReturnsUserWorkoutPreference() {
        when(userWorkoutPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(userWorkoutPreference));

        UserWorkoutPreference result = userWorkoutPreferenceService.getUserWorkoutPreferenceByUserId(1L);

        assertEquals(userWorkoutPreference, result);
        verify(userWorkoutPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserWorkoutPreferenceByUserId_UserDoesNotExist_ThrowsException() {
        when(userWorkoutPreferenceRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(UserWorkoutPreferenceNotFoundException.class, () -> userWorkoutPreferenceService.getUserWorkoutPreferenceByUserId(1L));
        verify(userWorkoutPreferenceRepository, times(1)).findByUserId(1L);
    }



    @Test
    void deleteUserWorkoutPreference_ExecutesWithoutException() {
        when(userWorkoutPreferenceRepository.getWorkoutPreferenceById(1L)).thenReturn(Optional.of(userWorkoutPreference));
        doNothing().when(userWorkoutPreferenceRepository).delete(1L);

        assertDoesNotThrow(() -> userWorkoutPreferenceService.deleteUserWorkoutPreference(1L));
        verify(userWorkoutPreferenceRepository, times(1)).getWorkoutPreferenceById(1L);
        verify(userWorkoutPreferenceRepository, times(1)).delete(1L);
    }

    @Test
    void deleteUserWorkoutPreference_UserWorkoutPreferenceNotFound_ThrowsException() {
        when(userWorkoutPreferenceRepository.getWorkoutPreferenceById(1L)).thenReturn(Optional.empty());

        assertThrows(UserWorkoutPreferenceNotFoundException.class, () -> userWorkoutPreferenceService.deleteUserWorkoutPreference(1L));
        verify(userWorkoutPreferenceRepository, times(1)).getWorkoutPreferenceById(1L);
        verify(userWorkoutPreferenceRepository, never()).delete(anyLong());
    }

    @Test
    void createUserWorkoutPreference_ReturnsCreatedUserWorkoutPreference() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference)).thenReturn(workoutPlan);
        when(workoutPlanService.createWorkoutPlan(workoutPlan)).thenReturn(workoutPlan);
        when(userWorkoutPreferenceRepository.create(userWorkoutPreference)).thenReturn(userWorkoutPreference);

        UserWorkoutPreference result = userWorkoutPreferenceService.createUserWorkoutPreference(userWorkoutPreference);

        assertEquals(userWorkoutPreference, result);
        verify(userService, times(1)).getUserById(1L);
        verify(workoutPlanGenerator, times(1)).calculateWorkoutPlan(userWorkoutPreference);
        verify(workoutPlanService, times(1)).createWorkoutPlan(workoutPlan);
        verify(userService, times(1)).updateUser(user);
        verify(userWorkoutPreferenceRepository, times(1)).create(userWorkoutPreference);
    }

    @Test
    void createUserWorkoutPreference_WorkoutPlanGeneratorReturnsNull_ThrowsException() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference)).thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> userWorkoutPreferenceService.createUserWorkoutPreference(userWorkoutPreference));

        verify(userService, times(1)).getUserById(1L);
        verify(workoutPlanGenerator, times(1)).calculateWorkoutPlan(userWorkoutPreference);
        verify(workoutPlanService, never()).createWorkoutPlan(any());
        verify(userService, never()).updateUser(any());
        verify(userWorkoutPreferenceRepository, never()).create(any());
    }

    @Test
    void updateUserWorkoutPreference_ReturnsUpdatedUserWorkoutPreference() {
        // Arrange
        when(userWorkoutPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(userWorkoutPreference)); // Fix: findByUserId
        when(userService.getUserById(1L)).thenReturn(user);
        when(workoutPlanService.getWorkoutPlanByUserId(1L)).thenReturn(workoutPlan); // Fix: Ensure existing plan is retrieved
        when(workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference)).thenReturn(updatedWorkoutPlan);
        when(workoutPlanService.updateWorkoutPlan(updatedWorkoutPlan)).thenReturn(updatedWorkoutPlan); // Fix: update instead of create
        when(userWorkoutPreferenceRepository.update(userWorkoutPreference)).thenReturn(userWorkoutPreference);

        // Act
        UserWorkoutPreference result = userWorkoutPreferenceService.updateUserWorkoutPreference(userWorkoutPreference);

        // Assert
        assertEquals(userWorkoutPreference, result);
        verify(userWorkoutPreferenceRepository, times(1)).findByUserId(1L); // Fix: Verify correct method
        verify(userService, times(1)).getUserById(1L);
        verify(workoutPlanService, times(1)).getWorkoutPlanByUserId(1L); // Verify retrieving existing plan
        verify(workoutPlanGenerator, times(1)).calculateWorkoutPlan(userWorkoutPreference);
        verify(workoutPlanService, times(1)).updateWorkoutPlan(updatedWorkoutPlan); // Fix: update instead of create
        verify(userService, times(1)).updateUser(user);
        verify(userWorkoutPreferenceRepository, times(1)).update(userWorkoutPreference);
    }

    @Test
    void updateUserWorkoutPreference_UserWorkoutPreferenceNotFound_ThrowsException() {
        // Arrange
        when(userWorkoutPreferenceRepository.findByUserId(1L)).thenReturn(Optional.empty()); // Fix: findByUserId

        // Act & Assert
        assertThrows(UserWorkoutPreferenceNotFoundException.class,
                () -> userWorkoutPreferenceService.updateUserWorkoutPreference(userWorkoutPreference));

        // Verify interactions
        verify(userWorkoutPreferenceRepository, times(1)).findByUserId(1L);
        verify(userService, never()).getUserById(anyLong());
        verify(workoutPlanGenerator, never()).calculateWorkoutPlan(any());
        verify(workoutPlanService, never()).updateWorkoutPlan(any()); // Fix: update instead of create
        verify(userService, never()).updateUser(any());
        verify(userWorkoutPreferenceRepository, never()).update(any());
    }


}
