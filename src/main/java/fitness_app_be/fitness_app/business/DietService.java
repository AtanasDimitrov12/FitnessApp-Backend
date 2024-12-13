package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;

import java.util.List;

public interface DietService {
    List<Diet> getAllDiets();
    Diet getDietById(Long id);
    Diet getDietByUserId(Long userId);
    Diet createDiet(Diet diet);
    void deleteDiet(Long id);
    Diet updateDiet(Diet diet);
    void addMealToDiet(long dietId, Meal meal);
    void removeMealFromDiet(long dietId, long mealId);
}
