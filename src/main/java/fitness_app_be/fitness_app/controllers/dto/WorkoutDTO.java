package fitness_app_be.fitness_app.controllers.dto;
import fitness_app_be.fitness_app.domain.enums.Exercises;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDTO {

    private Long id;
    private String name;
    private String description;
    private String pictureURL;
    private List<Exercises> exercises;

}