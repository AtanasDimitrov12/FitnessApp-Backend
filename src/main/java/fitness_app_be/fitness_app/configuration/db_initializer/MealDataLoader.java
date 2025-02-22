package fitness_app_be.fitness_app.configuration.db_initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MealDataLoader implements CommandLineRunner {

    private final MealServiceDBInit mealService;

    public MealDataLoader(MealServiceDBInit mealService) {
        this.mealService = mealService;
    }

    @Override
    public void run(String... args) throws Exception {
        mealService.populateMeals();
    }
}
