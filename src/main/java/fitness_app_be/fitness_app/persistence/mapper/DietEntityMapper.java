package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class DietEntityMapper {

    private MealEntityMapper mealEntityMapperImpl;
    private UserEntityMapper userEntityMapper;

    @Autowired
    public DietEntityMapper(@Lazy MealEntityMapper mealEntityMapperImpl, @Lazy UserEntityMapper userEntityMapper) {
        this.mealEntityMapperImpl = mealEntityMapperImpl;
        this.userEntityMapper = userEntityMapper;
    }

    public Diet toDomain(DietEntity dietEntity) {
        if (dietEntity == null) {
            return null;
        }

        return Diet.builder()
                .id(dietEntity.getId())
                .meals(dietEntity.getMeals() != null
                        ? dietEntity.getMeals().stream()
                        .map(mealEntityMapperImpl::toDomain)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .user(dietEntity.getUser() != null
                        ? userEntityMapper.toDomain(dietEntity.getUser())
                        : null)
                .build();
    }

    public DietEntity toEntity(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietEntity dietEntity = new DietEntity();
        dietEntity.setId(diet.getId());

        if (diet.getMeals() != null) {
            dietEntity.setMeals(diet.getMeals().stream()
                    .map(mealEntityMapperImpl::toEntity)
                    .collect(Collectors.toList()));
        }

        if (diet.getUser() != null) {
            dietEntity.setUser(userEntityMapper.toEntity(diet.getUser()));
        }

        return dietEntity;
    }
}
