package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Meal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MealService {
    @Transactional
    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    Meal createMeal(Meal meal);
    void deleteMeal(Long id);
    Meal updateMeal(Meal meal);
}
