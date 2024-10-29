package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DietMapper {

    private final MealMapper mealMapper;
    private final UserMapper userMapper;

    @Autowired
    public DietMapper(@Lazy MealMapper mealMapper, @Lazy UserMapper userMapper) {
        this.mealMapper = mealMapper;
        this.userMapper = userMapper;
    }

    public Diet toDomain(DietDTO dietDTO) {
        if (dietDTO == null) {
            return null;
        }

        return new Diet(
                dietDTO.getId(),
                dietDTO.getName(),
                dietDTO.getDescription(),
                dietDTO.getPictureURL(),
                dietDTO.getUsers() != null
                        ? dietDTO.getUsers().stream()
                        .map(userMapper::toDomain)
                        .collect(Collectors.toList())
                        : new ArrayList<>(),
                dietDTO.getMeals() != null
                        ? dietDTO.getMeals().stream()
                        .map(mealMapper::toDomain)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }

    public DietDTO domainToDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        return new DietDTO(
                diet.getId(),
                diet.getName(),
                diet.getDescription(),
                diet.getPictureURL(),
                diet.getUsers() != null
                        ? diet.getUsers().stream()
                        .map(userMapper::domainToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>(),
                diet.getMeals() != null
                        ? diet.getMeals().stream()
                        .map(mealMapper::domainToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}
