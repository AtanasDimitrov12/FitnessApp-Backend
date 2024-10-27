package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@NoArgsConstructor
public class DietEntityMapper {


    private MealEntityMapper mealEntityMapperImpl;
    private UserEntityMapper userEntityMapperImpl;

    public DietEntityMapper(MealEntityMapper mealEntityMapperImpl,@Lazy UserEntityMapper userEntityMapperImpl) {
        this.mealEntityMapperImpl = mealEntityMapperImpl;
        this.userEntityMapperImpl = userEntityMapperImpl;
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
                dietEntity.getMeals().stream().map(mealEntityMapperImpl::toDomain).collect(Collectors.toList()),
                dietEntity.getUsers().stream().map(userEntityMapperImpl::toDomain).collect(Collectors.toList())
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
        dietEntity.setMeals(diet.getMeals().stream().map(mealEntityMapperImpl::toEntity).collect(Collectors.toList()));
        dietEntity.setUsers(diet.getUsers().stream().map(userEntityMapperImpl::toEntity).collect(Collectors.toList()));

        return dietEntity;
    }
}
