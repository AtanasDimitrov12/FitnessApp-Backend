package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.exceptionHandling.MealNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.getAll();
    }

    @Override
    public Meal getMealById(Long id) {
        return mealRepository.getMealById(id)
                .orElseThrow(() -> new MealNotFoundException(id));
    }

    @Override
    public Meal createMeal(Meal meal) {
        return mealRepository.create(meal);
    }

    @Override
    public void deleteMeal(Long id) {
        if (!mealRepository.exists(id)) {
            throw new MealNotFoundException(id);
        }
        mealRepository.delete(id);
    }

    @Override
    public Meal updateMeal(Meal meal) {
        Meal existingMeal = mealRepository.getMealById(meal.getId())
                .orElseThrow(() -> new MealNotFoundException("Meal with ID " + meal.getId() + " not found"));

        existingMeal.setName(meal.getName());
        existingMeal.setCalories(meal.getCalories());
        existingMeal.setProtein(meal.getProtein());
        existingMeal.setCarbs(meal.getCarbs());
        existingMeal.setCookingTime(meal.getCookingTime());

        return mealRepository.update(existingMeal);
    }
}
