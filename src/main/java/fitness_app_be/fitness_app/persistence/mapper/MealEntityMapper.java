package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class MealEntityMapper {

    private DietEntityMapper dietEntityMapper;

    @Autowired
    public MealEntityMapper(@Lazy DietEntityMapper dietEntityMapper) {
        this.dietEntityMapper = dietEntityMapper;
    }

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
                mealEntity.getDiets() != null
                        ? mealEntity.getDiets().stream()
                        .map(dietEntityMapper::toDomain)
                        .collect(Collectors.toList())
                        : null
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

        if (meal.getDiets() != null) {
            mealEntity.setDiets(
                    meal.getDiets().stream()
                            .map(dietEntityMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return mealEntity;
    }
}
