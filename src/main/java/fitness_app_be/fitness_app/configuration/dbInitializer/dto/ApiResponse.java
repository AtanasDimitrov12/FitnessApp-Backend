package fitness_app_be.fitness_app.configuration.dbInitializer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private List<ApiMeal> meals;
}
