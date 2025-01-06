package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietPlanService;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDietPreferenceServiceImplTest {

    @InjectMocks
    private UserDietPreferenceServiceImpl userDietPreferenceService;

    @Mock
    private UserDietPreferenceRepository userDietPreferenceRepository;

    @Mock
    private DietPlanService dietPlanService;

    @Mock
    private DietService dietService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserDietPreferenceByUserId_ShouldReturnPreference_WhenExists() {
        Long userId = 1L;
        UserDietPreference mockPreference = new UserDietPreference(1L, userId, 2000, 3);

        when(userDietPreferenceRepository.findByUserId(userId)).thenReturn(Optional.of(mockPreference));

        UserDietPreference result = userDietPreferenceService.getUserDietPreferenceByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userDietPreferenceRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getUserDietPreferenceByUserId_ShouldThrowException_WhenNotExists() {
        Long userId = 1L;

        when(userDietPreferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(UserDietPreferenceNotFoundException.class,
                () -> userDietPreferenceService.getUserDietPreferenceByUserId(userId));
    }

    @Test
    void createUserDietPreference_ShouldCreateAndReturnPreference() {
        Long userId = 1L;
        User user = new User(userId, "testUser", "test@example.com", "password", null, null, "pictureURL", null, null, Role.USER, null,  null, null, true);

        UserDietPreference preference = new UserDietPreference(1L, userId, 2000, 3);

        Diet calculatedDiet = new Diet(1L, 1L, new ArrayList<>());

        Diet createdDiet = new Diet(2L, userId, new ArrayList<>());

        when(userService.getUserById(userId)).thenReturn(user);
        when(dietPlanService.calculateDiet(preference)).thenReturn(calculatedDiet);
        when(dietService.createDiet(any(Diet.class))).thenReturn(createdDiet);
        when(userDietPreferenceRepository.create(any(UserDietPreference.class))).thenReturn(preference);

        UserDietPreference result = userDietPreferenceService.createUserDietPreference(preference);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userService, times(1)).getUserById(userId);
        verify(dietPlanService, times(1)).calculateDiet(preference);
        verify(dietService, times(1)).createDiet(any(Diet.class));
        verify(userDietPreferenceRepository, times(1)).create(any(UserDietPreference.class));
    }

    @Test
    void deleteUserDietPreference_ShouldDelete_WhenExists() {
        Long preferenceId = 1L;
        UserDietPreference mockPreference = new UserDietPreference(1L,preferenceId, 2000, 3);

        when(userDietPreferenceRepository.getDietPreferenceById(preferenceId)).thenReturn(Optional.of(mockPreference));

        userDietPreferenceService.deleteUserDietPreference(preferenceId);

        verify(userDietPreferenceRepository, times(1)).getDietPreferenceById(preferenceId);
        verify(userDietPreferenceRepository, times(1)).delete(preferenceId);
    }

    @Test
    void deleteUserDietPreference_ShouldThrowException_WhenNotExists() {
        Long preferenceId = 1L;

        when(userDietPreferenceRepository.getDietPreferenceById(preferenceId)).thenReturn(Optional.empty());

        assertThrows(UserDietPreferenceNotFoundException.class,
                () -> userDietPreferenceService.deleteUserDietPreference(preferenceId));
    }

    @Test
    void updateUserDietPreference_ShouldUpdateAndReturnPreference() {
        Long userId = 1L;
        UserDietPreference preference = new UserDietPreference(1L,userId, 2500, 4);

        Diet recalculatedDiet = new Diet(1L,1L,new ArrayList<>());

        Diet existingDiet = new Diet(2L, userId, new ArrayList<>());

        User user = new User(userId, "testUser", "test@example.com", "password", null, null, null, null, null, null, null, null, null, true);

        when(dietPlanService.calculateDiet(preference)).thenReturn(recalculatedDiet);
        when(dietService.getDietByUserId(userId)).thenReturn(existingDiet);
        when(userService.getUserById(userId)).thenReturn(user);
        when(userDietPreferenceRepository.update(preference)).thenReturn(preference);

        UserDietPreference result = userDietPreferenceService.updateUserDietPreference(preference);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(dietPlanService, times(1)).calculateDiet(preference);
        verify(dietService, times(1)).getDietByUserId(userId);
        verify(userDietPreferenceRepository, times(1)).update(preference);
    }
}
