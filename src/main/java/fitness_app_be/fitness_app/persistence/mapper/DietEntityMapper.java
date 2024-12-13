package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
                .user(userEntityMapper.toDomain(dietEntity.getUser()))
                .meals(dietEntity.getMeals() == null ? Collections.emptyList()
                        : dietEntity.getMeals().stream()
                        .map(mealEntityMapper::toDomainWithoutDiets)
                        .toList())
                .build();
    }

    public DietEntity toEntity(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietEntity dietEntity = new DietEntity();
        dietEntity.setId(diet.getId());
        dietEntity.setUser(userEntityMapper.toEntity(diet.getUser()));
        dietEntity.setMeals(diet.getMeals() == null ? Collections.emptyList()
                : diet.getMeals().stream()
                .map(mealEntityMapper::toEntityWithoutDiets)
                .toList());
        return dietEntity;
    }
}
