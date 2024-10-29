package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.exceptionHandling.UserWorkoutPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserWorkoutPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWorkoutPreferenceServiceImplTest {

    @Mock
    private UserWorkoutPreferenceRepository userWorkoutPreferenceRepository;

    @InjectMocks
    private UserWorkoutPreferenceServiceImpl userWorkoutPreferenceService;

    private UserWorkoutPreference userWorkoutPreference;

    @BeforeEach
    void setUp() {
        userWorkoutPreference = new UserWorkoutPreference(1L, 1L, "Strength", "Beginner", "Evening", 4);
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
    void createUserWorkoutPreference_ReturnsCreatedUserWorkoutPreference() {
        when(userWorkoutPreferenceRepository.create(userWorkoutPreference)).thenReturn(userWorkoutPreference);

        UserWorkoutPreference result = userWorkoutPreferenceService.createUserWorkoutPreference(userWorkoutPreference);

        assertEquals(userWorkoutPreference, result);
        verify(userWorkoutPreferenceRepository, times(1)).create(userWorkoutPreference);
    }

    @Test
    void deleteUserWorkoutPreference_ExecutesWithoutException() {
        doNothing().when(userWorkoutPreferenceRepository).delete(1L);

        assertDoesNotThrow(() -> userWorkoutPreferenceService.deleteUserWorkoutPreference(1L));
        verify(userWorkoutPreferenceRepository, times(1)).delete(1L);
    }

    @Test
    void updateUserWorkoutPreference_ReturnsUpdatedUserWorkoutPreference() {
        when(userWorkoutPreferenceRepository.update(userWorkoutPreference)).thenReturn(userWorkoutPreference);

        UserWorkoutPreference result = userWorkoutPreferenceService.updateUserWorkoutPreference(userWorkoutPreference);

        assertEquals(userWorkoutPreference, result);
        verify(userWorkoutPreferenceRepository, times(1)).update(userWorkoutPreference);
    }
}
