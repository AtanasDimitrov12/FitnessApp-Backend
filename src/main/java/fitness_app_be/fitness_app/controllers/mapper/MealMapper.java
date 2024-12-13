package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class MealMapper {

    public Meal toDomain(MealDTO mealDTO) {
        if (mealDTO == null) {
            return null;
        }

        return new Meal(
                mealDTO.getId(),
                mealDTO.getName(),
                mealDTO.getCalories(),
                mealDTO.getProtein(),
                mealDTO.getCarbs(),
                mealDTO.getCookingTime()
        );
    }

    public MealDTO domainToDto(Meal meal) {
        if (meal == null) {
            return null;
        }

        return new MealDTO(
                meal.getId(),
                meal.getName(),
                meal.getCalories(),
                meal.getProtein(),
                meal.getCarbs(),
                meal.getCookingTime()
        );
    }
}
