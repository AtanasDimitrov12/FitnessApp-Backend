package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


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
                .name(dietEntity.getName())
                .description(dietEntity.getDescription())
                .pictureURL(dietEntity.getPictureURL())
                .meals(dietEntity.getMeals() != null
                        ? dietEntity.getMeals().stream()
                        .map(mealEntityMapperImpl::toDomain)
                        .toList()
                        : null)
                .users(dietEntity.getUsers() != null
                        ? dietEntity.getUsers().stream()
                        .map(userEntityMapper::toDomain)
                        .toList()
                        : null)
                .build();
    }

    public DietEntity toEntity(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietEntity dietEntity = new DietEntity();
        dietEntity.setId(diet.getId());
        dietEntity.setName(diet.getName());
        dietEntity.setDescription(diet.getDescription());
        dietEntity.setPictureURL(diet.getPictureURL());

        if (diet.getMeals() != null) {
            dietEntity.setMeals(diet.getMeals().stream()
                    .map(mealEntityMapperImpl::toEntity)
                    .toList());
        }

        if (diet.getUsers() != null) {
            dietEntity.setUsers(diet.getUsers().stream()
                    .map(userEntityMapper::toEntity)
                    .toList());
        }

        return dietEntity;
    }
}
