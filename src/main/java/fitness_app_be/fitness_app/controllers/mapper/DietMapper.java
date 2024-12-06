package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.domain.Diet;
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

        return Diet.builder()
                .id(dietDTO.getId())
                .user(dietDTO.getUser() != null ? userMapper.toDomain(dietDTO.getUser()) : null) // Map single User
                .meals(dietDTO.getMeals() != null
                        ? dietDTO.getMeals().stream()
                        .map(mealMapper::toDomain)
                        .toList()
                        : new ArrayList<>())
                .build();
    }

    public DietDTO domainToDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        return DietDTO.builder()
                .id(diet.getId())
                .user(diet.getUser() != null ? userMapper.domainToDto(diet.getUser()) : null) // Map single User
                .meals(diet.getMeals() != null
                        ? diet.getMeals().stream()
                        .map(mealMapper::domainToDto) // Correct mapping to MealDTO
                        .collect(Collectors.toList()) // Use collect to gather into List<MealDTO>
                        : new ArrayList<>())
                .build();
    }

}
