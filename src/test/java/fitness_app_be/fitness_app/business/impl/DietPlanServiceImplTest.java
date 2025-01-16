package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietPlanServiceImplTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private DietPlanServiceImpl dietPlanService;

    private UserDietPreference dietPreference;
    private List<Meal> mockMeals;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dietPreference = new UserDietPreference();
        dietPreference.setCalories(2000);
        dietPreference.setMealFrequency(3);

        mockMeals = Arrays.asList(
                new Meal(1L, "Chicken", 600, 40, 10, 30),
                new Meal(2L, "Rice", 500, 10, 50, 5),
                new Meal(3L, "Salad", 300, 5, 20, 2),
                new Meal(4L, "Omelet", 400, 20, 5, 15),
                new Meal(5L, "Smoothie", 700, 15, 40, 10)
        );
    }

    @Test
    void calculateDiet_ShouldReturnValidDiet_WhenMealsMatchPreference() {
        when(mealService.getAllMeals()).thenReturn(mockMeals);

        Diet bestDiet = dietPlanService.calculateDiet(dietPreference);

        assertNotNull(bestDiet, "Best diet plan should not be null.");
        assertFalse(bestDiet.getMeals().isEmpty(), "Diet should contain meals.");
        assertTrue(bestDiet.getMeals().size() <= dietPreference.getMealFrequency(), "Should respect meal frequency.");

        int totalCalories = bestDiet.getMeals().stream().mapToInt(Meal::getCalories).sum();
        assertTrue(totalCalories <= dietPreference.getCalories(), "Total calories should be within range.");
    }

    @Test
    void calculateDiet_ShouldReturnNull_WhenNoMealsMatchPreference() {
        when(mealService.getAllMeals()).thenReturn(Collections.emptyList());

        Diet bestDiet = dietPlanService.calculateDiet(dietPreference);

        assertNull(bestDiet, "Should return null when no meals are available.");
    }

    @Test
    void calculateDiet_ShouldHandleExactMatchForCaloricNeeds() {
        List<Meal> exactMatchMeals = Arrays.asList(
                new Meal(6L, "Meal 1", 1000, 50, 20, 30),
                new Meal(7L, "Meal 2", 1000, 40, 30, 20)
        );
        when(mealService.getAllMeals()).thenReturn(exactMatchMeals);

        Diet bestDiet = dietPlanService.calculateDiet(dietPreference);

        assertNotNull(bestDiet, "Best diet should be found.");
        assertEquals(2, bestDiet.getMeals().size(), "Should select two meals to match calorie target.");
    }

    @Test
    void calculateDiet_ShouldReturnLowestDeviationDiet_WhenMultipleOptionsExist() {
        List<Meal> variedMeals = Arrays.asList(
                new Meal(8L, "Meal A", 900, 40, 30, 15),
                new Meal(9L, "Meal B", 1100, 50, 20, 25),
                new Meal(10L, "Meal C", 2000, 80, 40, 50) // This should be selected
        );
        when(mealService.getAllMeals()).thenReturn(variedMeals);

        Diet bestDiet = dietPlanService.calculateDiet(dietPreference);

        assertNotNull(bestDiet);
        assertEquals(2000, bestDiet.getMeals().stream().mapToInt(Meal::getCalories).sum(), "Should pick the exact match meal.");
    }

    @Test
    void calculateDiet_ShouldRespectMealFrequencyLimit() {
        dietPreference.setMealFrequency(2);
        when(mealService.getAllMeals()).thenReturn(mockMeals);

        Diet bestDiet = dietPlanService.calculateDiet(dietPreference);

        assertNotNull(bestDiet, "Best diet should not be null.");
        assertTrue(bestDiet.getMeals().size() <= 2, "Meal frequency should be limited to 2.");
    }
}
