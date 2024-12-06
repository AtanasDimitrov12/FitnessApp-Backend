package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietPlanService;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
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

    @Mock
    private DietPlanService dietPlanService;

    @Mock
    private DietService dietService;

    @InjectMocks
    private UserDietPreferenceServiceImpl userDietPreferenceService;

    private UserDietPreference userDietPreference;
    private User user;
    private Diet userDiet;
    private Diet oldDiet;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testUser").diet(null).build();
        userDietPreference = new UserDietPreference(1L, user, 2500, 3);
        userDiet = Diet.builder().id(1L).user(user).build();
        oldDiet = new Diet(1L, user, null);
    }

    @Test
    void createUserDietPreference_ShouldCreateUserDietPreferenceAndAssociateDiet() {
        when(dietPlanService.calculateDiet(userDietPreference)).thenReturn(userDiet);
        when(dietService.createDiet(userDiet)).thenReturn(userDiet);
        when(userDietPreferenceRepository.create(userDietPreference)).thenReturn(userDietPreference);

        UserDietPreference result = userDietPreferenceService.createUserDietPreference(userDietPreference);

        assertNotNull(result);
        assertEquals(userDietPreference, result);

        verify(dietPlanService, times(1)).calculateDiet(userDietPreference);
        verify(dietService, times(1)).createDiet(userDiet);
        verify(userDietPreferenceRepository, times(1)).create(userDietPreference);
    }

    @Test
    void updateUserDietPreference_ShouldUpdateDietPreferenceAndRecalculateDiet() {
        when(userDietPreferenceRepository.update(userDietPreference)).thenReturn(userDietPreference);
        when(dietService.getDietByUserId(user.getId())).thenReturn(oldDiet);
        when(dietPlanService.calculateDiet(userDietPreference)).thenReturn(userDiet);

        UserDietPreference result = userDietPreferenceService.updateUserDietPreference(userDietPreference);

        assertNotNull(result);
        assertEquals(userDietPreference, result);

        verify(userDietPreferenceRepository, times(1)).update(userDietPreference);
        verify(dietService, times(1)).getDietByUserId(user.getId());
        verify(dietPlanService, times(1)).calculateDiet(userDietPreference);
        verify(dietService, times(1)).updateDiet(any(Diet.class));
    }

    @Test
    void getUserDietPreferenceByUserId_ShouldReturnUserDietPreference_WhenExists() {
        when(userDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(userDietPreference));

        UserDietPreference result = userDietPreferenceService.getUserDietPreferenceByUserId(1L);

        assertNotNull(result);
        assertEquals(userDietPreference, result);
        verify(userDietPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserDietPreferenceByUserId_ShouldThrowException_WhenNotFound() {
        when(userDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(UserDietPreferenceNotFoundException.class, () -> userDietPreferenceService.getUserDietPreferenceByUserId(1L));
        verify(userDietPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void deleteUserDietPreference_ShouldDeleteDietPreference() {
        doNothing().when(userDietPreferenceRepository).delete(1L);

        assertDoesNotThrow(() -> userDietPreferenceService.deleteUserDietPreference(1L));
        verify(userDietPreferenceRepository, times(1)).delete(1L);
    }
}
