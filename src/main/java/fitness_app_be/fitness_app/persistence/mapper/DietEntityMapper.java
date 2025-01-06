package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DietEntityMapper {

    private final MealEntityMapper mealEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public Diet toDomain(DietEntity dietEntity) {
        if (dietEntity == null) {
            return null;
        }

        return Diet.builder()
                .id(dietEntity.getId())
                .userId(dietEntity.getUserId())
                .meals(dietEntity.getMeals() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(dietEntity.getMeals().stream()
                        .map(mealEntityMapper::toDomain)
                        .toList())) // Ensure the result is mutable
                .build();
    }

    public DietEntity toEntity(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietEntity dietEntity = new DietEntity();
        dietEntity.setId(diet.getId());
        dietEntity.setUserId(diet.getUserId());
        dietEntity.setMeals(diet.getMeals() == null
                ? new ArrayList<>()
                : new ArrayList<>(diet.getMeals().stream()
                .map(mealEntityMapper::toEntityWithoutDiets)
                .toList()));
        return dietEntity;
    }

}
