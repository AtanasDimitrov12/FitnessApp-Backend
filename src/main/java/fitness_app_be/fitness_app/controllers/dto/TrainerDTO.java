package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.Workout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    private String gender;
    private String expertise;
    private String pictureURL;
    private List<WorkoutDTO> workoutsCreated;
    private List<DietDTO> dietsCreated;
}
