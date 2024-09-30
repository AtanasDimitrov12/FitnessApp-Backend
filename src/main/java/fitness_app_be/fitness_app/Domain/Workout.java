package fitness_app_be.fitness_app.Domain;

import fitness_app_be.fitness_app.Domain.Enums.Exercises;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    private Long id;
    private String name;
    private String description;
    private List<Exercises> exercises;

}