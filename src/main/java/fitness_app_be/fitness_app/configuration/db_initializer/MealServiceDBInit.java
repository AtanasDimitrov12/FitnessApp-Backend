package fitness_app_be.fitness_app.configuration.db_initializer;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiMeal;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiResponse;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class MealServiceDBInit {

    private final MealService mealService;
    private final RestTemplate restTemplate;

    public void populateMeals() {
        long mealCount = mealService.getAllMeals().stream().count();
        if (mealCount >= 50) {
            return;
        }

        List<Meal> meals = new ArrayList<>();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?f=" + alphabet;
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);

            if (response != null && response.getMeals() != null) {
                for (ApiMeal apiMeal : response.getMeals()) {
                    Meal meal = new Meal();
                    meal.setName(apiMeal.getStrMeal());
                    meal.setCookingTime(15 + (Math.random() * 5)); // Random 15-20 mins

                    // Randomly set calories, protein, and carbs
                    meal.setCalories(ThreadLocalRandom.current().nextInt(200, 800)); // 200-800 calories
                    meal.setProtein(ThreadLocalRandom.current().nextInt(10, 50));    // 10-50 grams of protein
                    meal.setCarbs(ThreadLocalRandom.current().nextInt(20, 100));    // 20-100 grams of carbs

                    meals.add(meal);
                }
            }
        }

        for (Meal newMeal : meals) {
            mealService.createMeal(newMeal);
        }
    }
}
