package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.exceptionHandling.MealNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealServiceImpl mealServiceImpl;

    private Meal mockMeal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMeal = new Meal(1L, 1L, "Pasta", 400, 15, 50, 20.0);
    }

    @Test
    void getAllMeals() {
        List<Meal> meals = Arrays.asList(mockMeal);
        when(mealRepository.getAll()).thenReturn(meals);

        List<Meal> result = mealServiceImpl.getAllMeals();

        assertNotNull(result, "The list of meals should not be null.");
        assertEquals(1, result.size(), "The size of the meal list does not match.");
        assertEquals("Pasta", result.get(0).getName(), "The meal name does not match.");

        verify(mealRepository, times(1)).getAll();
    }

    @Test
    void getMealById() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.of(mockMeal));

        Meal meal = mealServiceImpl.getMealById(1L);

        assertNotNull(meal, "The meal should not be null.");
        assertEquals("Pasta", meal.getName(), "The meal name does not match.");

        verify(mealRepository, times(1)).getMealById(1L);
    }

    @Test
    void getMealById_MealNotFound() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> mealServiceImpl.getMealById(1L), "Expected MealNotFoundException");

        verify(mealRepository, times(1)).getMealById(1L);
    }

    @Test
    void createMeal() {
        when(mealRepository.create(mockMeal)).thenReturn(mockMeal);

        Meal createdMeal = mealServiceImpl.createMeal(mockMeal);

        assertNotNull(createdMeal, "The created meal should not be null.");
        assertEquals("Pasta", createdMeal.getName(), "The meal name does not match.");

        verify(mealRepository, times(1)).create(mockMeal);
    }

    @Test
    void deleteMeal() {
        when(mealRepository.exists(1L)).thenReturn(true);

        mealServiceImpl.deleteMeal(1L);

        verify(mealRepository, times(1)).delete(1L);
    }

    @Test
    void deleteMeal_MealNotFound() {
        when(mealRepository.exists(1L)).thenReturn(false);

        assertThrows(MealNotFoundException.class, () -> mealServiceImpl.deleteMeal(1L), "Expected MealNotFoundException");

        verify(mealRepository, times(1)).exists(1L);
        verify(mealRepository, never()).delete(1L);
    }

    @Test
    void updateMeal() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.of(mockMeal));
        when(mealRepository.update(mockMeal)).thenReturn(mockMeal);

        mockMeal.setName("Updated Pasta");

        Meal updatedMeal = mealServiceImpl.updateMeal(mockMeal);

        assertNotNull(updatedMeal, "The updated meal should not be null.");
        assertEquals("Updated Pasta", updatedMeal.getName(), "The meal name was not updated correctly.");

        verify(mealRepository, times(1)).getMealById(1L);
        verify(mealRepository, times(1)).update(mockMeal);
    }

    @Test
    void updateMeal_MealNotFound() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.empty());

        mockMeal.setName("Updated Pasta");

        assertThrows(MealNotFoundException.class, () -> mealServiceImpl.updateMeal(mockMeal), "Expected MealNotFoundException");

        verify(mealRepository, times(1)).getMealById(1L);
        verify(mealRepository, never()).update(mockMeal);
    }
}
