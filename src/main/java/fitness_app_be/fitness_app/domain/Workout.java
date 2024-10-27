package fitness_app_be.fitness_app.domain;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    @Setter(AccessLevel.NONE)
    private Long id;
    private Long trainerId;
    private String name;
    private String description;
    private String pictureURL;
    private List<String> exercises;
    private List<User> users ;
}