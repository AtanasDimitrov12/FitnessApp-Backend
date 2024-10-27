package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor()
public class Trainer {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    private String gender;
    private String expertise;
    private String pictureURL;
    private List<Workout> workoutsCreated = new ArrayList<>();
    private List<Diet> dietsCreated;
}
