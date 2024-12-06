package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietPlanService;
import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietPlanServiceImpl implements DietPlanService {

    private final MealService mealService;

    @Override
    public Diet calculateDiet(UserDietPreference dietPreference) {
        // Step 1: Fetch all eligible meals
        List<Meal> eligibleMeals = findMatchingMeals(dietPreference);

        // Step 2: Create diet plans based on eligible meals
        List<Diet> generatedDietPlans = generateDietPlans(eligibleMeals, dietPreference);

        // Step 3: Find and return the best matching diet plan
        return findBestDietPlan(generatedDietPlans, dietPreference);
    }

    private List<Meal> findMatchingMeals(UserDietPreference dietPreference) {
        // Fetch all meals from the service
        List<Meal> allMeals = mealService.getAllMeals();

        // Filter meals based on user preferences
        return allMeals.stream()
                .filter(meal -> meal.getCalories() <= dietPreference.getCalories() / dietPreference.getMealFrequency())
                .collect(Collectors.toList());
    }

    private List<Diet> generateDietPlans(List<Meal> meals, UserDietPreference dietPreference) {
        List<Diet> dietPlans = new ArrayList<>();
        int mealFrequency = dietPreference.getMealFrequency();
        int targetCalories = dietPreference.getCalories();

        // Generate combinations of meals to create diet plans
        for (int i = 0; i < meals.size(); i++) {
            List<Meal> dietPlanMeals = new ArrayList<>();
            int totalCalories = 0;

            for (int j = 0; j < mealFrequency && i + j < meals.size(); j++) {
                Meal meal = meals.get(i + j);
                totalCalories += meal.getCalories();
                dietPlanMeals.add(meal);

                // Stop adding meals if calorie target is reached
                if (totalCalories >= targetCalories) {
                    break;
                }
            }

            // Create a diet plan if it meets the calorie target
            if (totalCalories <= targetCalories) {
                Diet diet = new Diet();
                diet.setMeals(dietPlanMeals);
                dietPlans.add(diet);
            }
        }
        return dietPlans;
    }

    private Diet findBestDietPlan(List<Diet> dietPlans, UserDietPreference dietPreference) {
        // Find the single best diet plan by closeness to the calorie target
        return dietPlans.stream()
                .min(Comparator.comparingInt(diet -> Math.abs(dietPreference.getCalories() - calculateTotalCalories(diet.getMeals()))))
                .orElse(null); // Return null if no diet plan is found
    }

    private int calculateTotalCalories(List<Meal> meals) {
        return meals.stream().mapToInt(Meal::getCalories).sum();
    }
}
