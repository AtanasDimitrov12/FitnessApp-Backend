package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.domain.Meal;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {

    public Meal toDomain(MealDTO mealDTO) {
        return new Meal(mealDTO.getId(), mealDTO.getTrainerId(), mealDTO.getName(),
                mealDTO.getCalories(), mealDTO.getProtein(), mealDTO.getCarbs(),
                mealDTO.getCookingTime());
    }

    public MealDTO domainToDto(Meal meal) {
        return new MealDTO(meal.getId(), meal.getTrainerId(), meal.getName(),
                meal.getCalories(), meal.getProtein(), meal.getCarbs(),
                meal.getCookingTime());
    }
}
