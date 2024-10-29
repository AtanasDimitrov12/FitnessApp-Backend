package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exceptionHandling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
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
class UserDietPreferenceServiceImplTest {

    @Mock
    private UserDietPreferenceRepository userDietPreferenceRepository;

    @InjectMocks
    private UserDietPreferenceServiceImpl userDietPreferenceService;

    private UserDietPreference userDietPreference;

    @BeforeEach
    void setUp() {
        userDietPreference = new UserDietPreference(1L, 1L, 2500, 3);
    }

    @Test
    void getUserDietPreferenceByUserId_UserExists_ReturnsUserDietPreference() {
        when(userDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(userDietPreference));

        UserDietPreference result = userDietPreferenceService.getUserDietPreferenceByUserId(1L);

        assertEquals(userDietPreference, result);
        verify(userDietPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserDietPreferenceByUserId_UserDoesNotExist_ThrowsException() {
        when(userDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(UserDietPreferenceNotFoundException.class, () -> userDietPreferenceService.getUserDietPreferenceByUserId(1L));
        verify(userDietPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createUserDietPreference_ReturnsCreatedUserDietPreference() {
        when(userDietPreferenceRepository.create(userDietPreference)).thenReturn(userDietPreference);

        UserDietPreference result = userDietPreferenceService.createUserDietPreference(userDietPreference);

        assertEquals(userDietPreference, result);
        verify(userDietPreferenceRepository, times(1)).create(userDietPreference);
    }

    @Test
    void deleteUserDietPreference_ExecutesWithoutException() {
        doNothing().when(userDietPreferenceRepository).delete(1L);

        assertDoesNotThrow(() -> userDietPreferenceService.deleteUserDietPreference(1L));
        verify(userDietPreferenceRepository, times(1)).delete(1L);
    }

    @Test
    void updateUserDietPreference_ReturnsUpdatedUserDietPreference() {
        when(userDietPreferenceRepository.update(userDietPreference)).thenReturn(userDietPreference);

        UserDietPreference result = userDietPreferenceService.updateUserDietPreference(userDietPreference);

        assertEquals(userDietPreference, result);
        verify(userDietPreferenceRepository, times(1)).update(userDietPreference);
    }
}
