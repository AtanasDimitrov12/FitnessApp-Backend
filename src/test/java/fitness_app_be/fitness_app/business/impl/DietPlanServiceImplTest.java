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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietPlanServiceImplTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private DietPlanServiceImpl dietPlanService;

    private UserDietPreference dietPreference;
    private List<Meal> meals;

    @BeforeEach
    void setUp() {
        dietPreference = new UserDietPreference(1L, 1L, 2000, 3);
        meals = List.of(
                new Meal(1L, "Meal1", 500, 20, 50, 30),
                new Meal(2L, "Meal2", 600, 25, 60, 35),
                new Meal(3L, "Meal3", 700, 30, 70, 40)
        );
    }

    @Test
    void calculateDiet_ShouldReturnBestDietPlan_WhenMealsMatchPreference() {
        when(mealService.getAllMeals()).thenReturn(meals);

        Diet result = dietPlanService.calculateDiet(dietPreference);

        assertNotNull(result);
        assertFalse(result.getMeals().isEmpty());
        verify(mealService, times(1)).getAllMeals();
    }

    @Test
    void calculateDiet_ShouldReturnNull_WhenNoMatchingMeals() {
        when(mealService.getAllMeals()).thenReturn(new ArrayList<>());

        Diet result = dietPlanService.calculateDiet(dietPreference);

        assertNull(result);
        verify(mealService, times(1)).getAllMeals();
    }

    @Test
    void findMatchingMeals_ShouldReturnFilteredMeals() {
        when(mealService.getAllMeals()).thenReturn(meals);

        List<Meal> filteredMeals = dietPlanService.calculateDiet(dietPreference).getMeals();

        assertNotNull(filteredMeals);
        assertFalse(filteredMeals.isEmpty());
    }


}
