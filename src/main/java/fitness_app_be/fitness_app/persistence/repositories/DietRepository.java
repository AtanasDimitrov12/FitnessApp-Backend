package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;

import java.util.List;
import java.util.Optional;

public interface DietRepository {
    boolean exists(long id);

    List<Diet> getAll();

    Diet create(Diet diet);

    Diet update(Diet diet);

    void delete(long dietId);

    Optional<Diet> getDietById(long dietId);

    Optional<Diet> getDietByUserId(long userId);

    void addMealToDiet(Long dietId, Meal meal);

    void removeMealFromDiet(long dietId, long mealId);
}
