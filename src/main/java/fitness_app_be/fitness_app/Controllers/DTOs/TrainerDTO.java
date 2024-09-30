package fitness_app_be.fitness_app.Controllers.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
