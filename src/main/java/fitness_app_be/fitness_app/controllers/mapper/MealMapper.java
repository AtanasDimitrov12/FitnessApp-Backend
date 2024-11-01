package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.domain.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MealMapper {

    private final DietMapper dietMapper;

    @Autowired
    public MealMapper(@Lazy DietMapper dietMapper) {
        this.dietMapper = dietMapper;
    }

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
                mealDTO.getCookingTime(),
                mealDTO.getDiets() != null
                        ? mealDTO.getDiets().stream()
                        .map(dietMapper::toDomain)
                        .toList()
                        : new ArrayList<>()
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
                meal.getCookingTime(),
                meal.getDiets() != null
                        ? meal.getDiets().stream()
                        .map(dietMapper::domainToDto)
                        .toList()
                        : new ArrayList<>()
        );
    }
}
