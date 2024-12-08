package fitness_app_be.fitness_app.configuration.DBInitializer;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.configuration.DBInitializer.dto.ApiMeal;
import fitness_app_be.fitness_app.configuration.DBInitializer.dto.ApiResponse;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MealServiceDBInit {

    private final MealService mealService;
    private final RestTemplate restTemplate;


    public void populateMeals() {
        long mealCount = mealService.getAllMeals().stream().count();
        if (mealCount >= 50) {
            System.out.println("Database already has 50 or more meals. Skipping population.");
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
                    meals.add(meal);
                }
            }
        }

        for (Meal newMeal : meals) {
            mealService.createMeal(newMeal);
        }

        System.out.println("Meals have been populated in the database.");
    }
}