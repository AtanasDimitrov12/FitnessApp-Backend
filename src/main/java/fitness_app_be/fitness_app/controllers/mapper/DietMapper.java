package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.domain.Diet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DietMapper {

    private final MealMapper mealMapper;

    @Autowired
    public DietMapper(@Lazy MealMapper mealMapper) {
        this.mealMapper = mealMapper;
    }

    public Diet toDomain(DietDTO dietDTO) {
        if (dietDTO == null) {
            return null;
        }

        return new Diet(
                dietDTO.getId(),
                dietDTO.getUserId(),
                dietDTO.getName(),
                dietDTO.getDescription(),
                dietDTO.getPictureURL(),
                dietDTO.getMeals() != null
                        ? dietDTO.getMeals().stream()
                        .map(mealMapper::toDomain)
                        .collect(Collectors.toList())
                        : null
        );
    }

    public DietDTO domainToDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        return new DietDTO(
                diet.getId(),
                diet.getUserId(),
                diet.getName(),
                diet.getDescription(),
                diet.getPictureURL(),
                diet.getMeals() != null
                        ? diet.getMeals().stream()
                        .map(mealMapper::domainToDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
