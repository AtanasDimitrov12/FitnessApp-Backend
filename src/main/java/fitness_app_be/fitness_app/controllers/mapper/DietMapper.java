package fitness_app_be.fitness_app.controllers.mapper;


import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.domain.Diet;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DietMapper {

    private final MealMapper mealMapper;
    private final UserMapper userMapper;

    public DietMapper(MealMapper mealMapper, UserMapper userMapper) {
        this.mealMapper = mealMapper;
        this.userMapper = userMapper;
    }

    public Diet toDomain(DietDTO dietDTO) {
        return new Diet(dietDTO.getId(), dietDTO.getTrainerId(), dietDTO.getName(),
                dietDTO.getDescription(), dietDTO.getPictureURL(),
                dietDTO.getMeals().stream().map(mealMapper::toDomain).collect(Collectors.toList()),
                dietDTO.getUsers().stream().map(userMapper::toDomain).collect(Collectors.toList()));
    }

    public DietDTO domainToDto(Diet diet) {
        return new DietDTO(diet.getId(), diet.getTrainerId(), diet.getName(),
                diet.getDescription(), diet.getPictureURL(),
                diet.getMeals().stream().map(mealMapper::domainToDto).collect(Collectors.toList()),
                diet.getUsers().stream().map(userMapper::domainToDto).collect(Collectors.toList()));
    }
}
