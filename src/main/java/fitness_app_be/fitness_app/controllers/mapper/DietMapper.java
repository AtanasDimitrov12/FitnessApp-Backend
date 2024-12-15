package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DietMapper {

    private final MealMapper mealMapper; // Inject the MealMapper for proper mapping

    public Diet toDomain(DietDTO dietDTO) {
        if (dietDTO == null) {
            return null;
        }

        return Diet.builder()
                .id(dietDTO.getId())
                .userId(dietDTO.getUserId())
                .meals(dietDTO.getMeals() == null ? new ArrayList<>() :
                        dietDTO.getMeals().stream()
                                .map(mealMapper::toDomain)
                                .collect(Collectors.toList()))
                .build();
    }

    public DietDTO domainToDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        return DietDTO.builder()
                .id(diet.getId())
                .userId(diet.getUserId())
                .meals(diet.getMeals() == null ? new ArrayList<>() :
                        diet.getMeals().stream()
                                .map(mealMapper::domainToDto)
                                .collect(Collectors.toList()))
                .build();
    }
}
