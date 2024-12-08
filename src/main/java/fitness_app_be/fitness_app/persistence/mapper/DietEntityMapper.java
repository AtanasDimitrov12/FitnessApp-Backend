package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DietEntityMapper {

    private final MealEntityMapper mealEntityMapper;

    public Diet toDomain(DietEntity dietEntity) {
        if (dietEntity == null) {
            return null;
        }

        return Diet.builder()
                .id(dietEntity.getId())
                .meals(dietEntity.getMeals() == null ? Collections.emptyList()
                        : dietEntity.getMeals().stream()
                        .map(mealEntityMapper::toDomainWithoutDiets)
                        .collect(Collectors.toList()))
                .build();
    }

    public DietEntity toEntity(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietEntity dietEntity = new DietEntity();
        dietEntity.setId(diet.getId());
        dietEntity.setMeals(diet.getMeals() == null ? Collections.emptyList()
                : diet.getMeals().stream()
                .map(mealEntityMapper::toEntityWithoutDiets)
                .collect(Collectors.toList()));
        return dietEntity;
    }
}
