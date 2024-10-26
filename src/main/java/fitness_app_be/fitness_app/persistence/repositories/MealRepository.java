package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    boolean exists(long id);

    List<Meal> getAll();

    Meal create(Meal meal);

    Meal update(Meal meal);

    void delete(long mealId);

    Optional<Meal> getMealById(long mealId);

    List<Meal> findByNameContainingIgnoreCase(String name);
}
