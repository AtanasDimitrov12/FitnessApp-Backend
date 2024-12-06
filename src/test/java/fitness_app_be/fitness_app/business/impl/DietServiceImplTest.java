package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.DietNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.DietRepository;
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
class DietServiceImplTest {

    @Mock
    private DietRepository dietRepository;

    @InjectMocks
    private DietServiceImpl dietService;

    private Diet diet;
    private User user;

    @BeforeEach
    void setUp() {
        List<Meal> meals = new ArrayList<>();
        user = new User();
        diet = new Diet(1L, user, meals);
    }

    @Test
    void getAllDiets_ShouldReturnListOfDiets() {
        when(dietRepository.getAll()).thenReturn(List.of(diet));

        List<Diet> diets = dietService.getAllDiets();

        assertNotNull(diets);
        assertEquals(1, diets.size());
        assertEquals(diet, diets.get(0));
        verify(dietRepository, times(1)).getAll();
    }

    @Test
    void getDietById_ShouldReturnDiet_WhenDietExists() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.of(diet));

        Diet foundDiet = dietService.getDietById(1L);

        assertNotNull(foundDiet);
        assertEquals(diet, foundDiet);
        verify(dietRepository, times(1)).getDietById(1L);
    }

    @Test
    void getDietById_ShouldThrowException_WhenDietNotFound() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.empty());

        assertThrows(DietNotFoundException.class, () -> dietService.getDietById(1L));
        verify(dietRepository, times(1)).getDietById(1L);
    }

    @Test
    void getDietByUserId_ShouldReturnDiet_WhenDietExists() {
        when(dietRepository.getDietByUserId(1L)).thenReturn(Optional.of(diet));

        Diet foundDiet = dietService.getDietByUserId(1L);

        assertNotNull(foundDiet);
        assertEquals(diet, foundDiet);
        verify(dietRepository, times(1)).getDietByUserId(1L);
    }

    @Test
    void getDietByUserId_ShouldThrowException_WhenDietNotFound() {
        when(dietRepository.getDietByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(DietNotFoundException.class, () -> dietService.getDietByUserId(1L));
        verify(dietRepository, times(1)).getDietByUserId(1L);
    }

    @Test
    void createDiet_ShouldReturnCreatedDiet() {
        when(dietRepository.create(any(Diet.class))).thenReturn(diet);

        Diet createdDiet = dietService.createDiet(diet);

        assertNotNull(createdDiet);
        assertEquals(diet, createdDiet);
        verify(dietRepository, times(1)).create(diet);
    }

    @Test
    void deleteDiet_ShouldDeleteDiet_WhenDietExists() {
        when(dietRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> dietService.deleteDiet(1L));
        verify(dietRepository, times(1)).exists(1L);
        verify(dietRepository, times(1)).delete(1L);
    }

    @Test
    void deleteDiet_ShouldThrowException_WhenDietNotFound() {
        when(dietRepository.exists(1L)).thenReturn(false);

        assertThrows(DietNotFoundException.class, () -> dietService.deleteDiet(1L));
        verify(dietRepository, times(1)).exists(1L);
        verify(dietRepository, never()).delete(1L);
    }

    @Test
    void updateDiet_ShouldReturnUpdatedDiet_WhenDietExists() {
        List<Meal> newMeals = new ArrayList<>();
        Diet updatedDiet = new Diet(1L, user, newMeals);

        when(dietRepository.getDietByUserId(1L)).thenReturn(Optional.of(diet));
        when(dietRepository.update(any(Diet.class))).thenReturn(updatedDiet);

        Diet result = dietService.updateDiet(updatedDiet);

        assertNotNull(result);
        assertEquals(updatedDiet.getMeals(), result.getMeals());
        verify(dietRepository, times(1)).getDietByUserId(1L);
        verify(dietRepository, times(1)).update(diet);
    }

    @Test
    void updateDiet_ShouldThrowException_WhenDietNotFound() {
        List<Meal> newMeals = new ArrayList<>();
        Diet updatedDiet = new Diet(1L, user, newMeals);

        when(dietRepository.getDietByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(DietNotFoundException.class, () -> dietService.updateDiet(updatedDiet));
        verify(dietRepository, times(1)).getDietByUserId(1L);
        verify(dietRepository, never()).update(any(Diet.class));
    }
}
