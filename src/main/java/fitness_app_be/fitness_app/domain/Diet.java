package fitness_app_be.fitness_app.domain;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diet {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String description;
    private String pictureURL;
    private List<User> users = new ArrayList<>();
    private List<Meal> meals = new ArrayList<>();
}
