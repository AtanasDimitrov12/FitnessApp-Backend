package fitness_app_be.fitness_app.configuration.DBInitializer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private List<ApiMeal> meals;
}
