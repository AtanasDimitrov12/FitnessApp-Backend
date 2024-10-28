package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.exceptionHandling.MealNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.MealRepository;
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
class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealServiceImpl mealService;

    private Meal meal;

    @BeforeEach
    void setUp() {
        meal = new Meal(1L, "Chicken Salad", 300, 25, 10, 15);

    }

    @Test
    void getAllMeals_ShouldReturnListOfMeals() {
        when(mealRepository.getAll()).thenReturn(List.of(meal));

        List<Meal> meals = mealService.getAllMeals();

        assertNotNull(meals);
        assertEquals(1, meals.size());
        assertEquals(meal, meals.get(0));
        verify(mealRepository, times(1)).getAll();
    }

    @Test
    void getMealById_ShouldReturnMeal_WhenMealExists() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.of(meal));

        Meal foundMeal = mealService.getMealById(1L);

        assertNotNull(foundMeal);
        assertEquals(meal, foundMeal);
        verify(mealRepository, times(1)).getMealById(1L);
    }

    @Test
    void getMealById_ShouldThrowException_WhenMealNotFound() {
        when(mealRepository.getMealById(1L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> mealService.getMealById(1L));
        verify(mealRepository, times(1)).getMealById(1L);
    }

    @Test
    void createMeal_ShouldReturnCreatedMeal() {
        when(mealRepository.create(any(Meal.class))).thenReturn(meal);

        Meal createdMeal = mealService.createMeal(meal);

        assertNotNull(createdMeal);
        assertEquals(meal, createdMeal);
        verify(mealRepository, times(1)).create(meal);
    }

    @Test
    void deleteMeal_ShouldDeleteMeal_WhenMealExists() {
        when(mealRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> mealService.deleteMeal(1L));
        verify(mealRepository, times(1)).exists(1L);
        verify(mealRepository, times(1)).delete(1L);
    }

    @Test
    void deleteMeal_ShouldThrowException_WhenMealNotFound() {
        when(mealRepository.exists(1L)).thenReturn(false);

        assertThrows(MealNotFoundException.class, () -> mealService.deleteMeal(1L));
        verify(mealRepository, times(1)).exists(1L);
        verify(mealRepository, never()).delete(1L);
    }

    @Test
    void updateMeal_ShouldReturnUpdatedMeal_WhenMealExists() {
        Meal updatedMeal = new Meal(1L, "Beef Salad", 300, 25, 10, 15);

        when(mealRepository.getMealById(1L)).thenReturn(Optional.of(meal));
        when(mealRepository.update(meal)).thenReturn(updatedMeal);

        Meal result = mealService.updateMeal(updatedMeal);

        assertNotNull(result);
        assertEquals("Beef Salad", result.getName());
        assertEquals(300, result.getCalories());
        verify(mealRepository, times(1)).getMealById(1L);
        verify(mealRepository, times(1)).update(meal);
    }

    @Test
    void updateMeal_ShouldThrowException_WhenMealNotFound() {
        Meal updatedMeal = new Meal(1L, "Beef Salad", 300, 25, 10, 15);

        when(mealRepository.getMealById(1L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> mealService.updateMeal(updatedMeal));
        verify(mealRepository, times(1)).getMealById(1L);
        verify(mealRepository, never()).update(any(Meal.class));
    }
}
