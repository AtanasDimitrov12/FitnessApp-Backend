package fitness_app_be.fitness_app.configuration.db_initializer;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiMeal;
import fitness_app_be.fitness_app.configuration.db_initializer.dto.ApiResponse;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MealServiceDBInit {

    private final MealService mealService;
    private final RestTemplate restTemplate;
    private final SecureRandom secureRandom = new SecureRandom(); // SecureRandom instance for tag selection


    public void populateMeals() {
        long mealCount = mealService.getAllMeals().stream().count();
        if (mealCount >= 50) {
            log.info("Meals already populated. Skipping initialization.");
            return;
        }

        List<Meal> meals = new ArrayList<>();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?f=" + alphabet;
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);

            if (response != null && response.getMeals() != null) {
                for (ApiMeal apiMeal : response.getMeals()) {
                    try {
                        Meal meal = new Meal();
                        meal.setName(apiMeal.getStrMeal());
                        meal.setCookingTime(15 + secureRandom.nextInt(6));


                        // Randomly set calories, protein, and carbs
                        meal.setCalories(secureRandom.nextInt(601) + 200); // 200-800 calories
                        meal.setProtein(secureRandom.nextInt(41) + 10);    // 10-50 grams of protein
                        meal.setCarbs(secureRandom.nextInt(81) + 20);      // 20-100 grams of carbs

                        meals.add(meal);
                    } catch (Exception e) {
                        log.error("Error processing meal: {}. Error: {}", apiMeal.getStrMeal(), e.getMessage());
                    }
                }
            }
        }

        meals.forEach(meal -> {
            try {
                mealService.createMeal(meal);
            } catch (Exception e) {
                log.error("Error saving meal: {}. Error: {}", meal.getName(), e.getMessage());
            }
        });
    }
}
