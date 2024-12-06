package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Diet;

public interface DietManagementService {
    void associateDietWithUser(Long userId, Diet newDiet);
}
