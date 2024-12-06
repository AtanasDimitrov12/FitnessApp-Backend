package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.UserDietPreference;

import java.util.List;

public interface DietPlanService {
    Diet calculateDiet(UserDietPreference dietPreference);
}
