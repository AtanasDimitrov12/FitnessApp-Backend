package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DietEntityMapper {

    private final MealEntityMapper mealEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public DietEntityMapper(MealEntityMapper mealEntityMapper, UserEntityMapper userEntityMapper) {
        this.mealEntityMapper = mealEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }

    public Diet toDomain(DietEntity dietEntity) {
        if (dietEntity == null) {
            return null;
        }
        return new Diet(
                dietEntity.getId(),
                dietEntity.getTrainer().getId(),
                dietEntity.getName(),
                dietEntity.getDescription(),
                dietEntity.getPictureURL(),
                dietEntity.getMeals().stream().map(mealEntityMapper::toDomain).collect(Collectors.toList()),
                dietEntity.getUsers().stream().map(userEntityMapper::toDomain).collect(Collectors.toList())
        );
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
        dietEntity.setMeals(diet.getMeals().stream().map(mealEntityMapper::toEntity).collect(Collectors.toList()));
        dietEntity.setUsers(diet.getUsers().stream().map(userEntityMapper::toEntity).collect(Collectors.toList()));

        return dietEntity;
    }
}
