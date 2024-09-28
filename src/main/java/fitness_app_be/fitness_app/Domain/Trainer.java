package fitness_app_be.fitness_app.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Trainer {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String gender;
    private String expertise;
}
