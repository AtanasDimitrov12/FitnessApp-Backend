package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class MealEntityMapper {

    public Meal toDomain(MealEntity mealEntity) {
        if (mealEntity == null) {
            return null;
        }

        return new Meal(
                mealEntity.getId(),
                mealEntity.getName(),
                mealEntity.getCalories(),
                mealEntity.getProtein(),
                mealEntity.getCarbs(),
                mealEntity.getCookingTime(),
                mealEntity.getDiets() == null ? Collections.emptyList() : Collections.emptyList()
        );
    }

    public Meal toDomainWithoutDiets(MealEntity mealEntity) {
        if (mealEntity == null) {
            return null;
        }

        return new Meal(
                mealEntity.getId(),
                mealEntity.getName(),
                mealEntity.getCalories(),
                mealEntity.getProtein(),
                mealEntity.getCarbs(),
                mealEntity.getCookingTime(),
                Collections.emptyList() // Prevent cycles
        );
    }

    public MealEntity toEntity(Meal meal) {
        if (meal == null) {
            return null;
        }

        MealEntity mealEntity = new MealEntity();
        mealEntity.setId(meal.getId());
        mealEntity.setName(meal.getName());
        mealEntity.setCalories(meal.getCalories());
        mealEntity.setProtein(meal.getProtein());
        mealEntity.setCarbs(meal.getCarbs());
        mealEntity.setCookingTime(meal.getCookingTime());
        mealEntity.setDiets(Collections.emptyList()); // Prevent cycles
        return mealEntity;
    }

    public MealEntity toEntityWithoutDiets(Meal meal) {
        if (meal == null) {
            return null;
        }

        MealEntity mealEntity = new MealEntity();
        mealEntity.setId(meal.getId());
        mealEntity.setName(meal.getName());
        mealEntity.setCalories(meal.getCalories());
        mealEntity.setProtein(meal.getProtein());
        mealEntity.setCarbs(meal.getCarbs());
        mealEntity.setCookingTime(meal.getCookingTime());
        return mealEntity;
    }
}
