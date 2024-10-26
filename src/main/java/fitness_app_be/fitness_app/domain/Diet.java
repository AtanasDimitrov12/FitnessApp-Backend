package fitness_app_be.fitness_app.domain;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diet {

    @Setter(AccessLevel.NONE)
    private Long id;
    @Setter(AccessLevel.NONE)
    private Long trainerId;
    private String name;
    private String description;
    private String pictureURL;
    private List<Meal> meals;
    private List<User> users;
}
