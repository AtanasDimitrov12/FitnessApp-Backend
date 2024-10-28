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
    @Setter(AccessLevel.NONE)
    private Long userId;
    private String name;
    private String description;
    private String pictureURL;
    private List<Meal> meals = new ArrayList<>();
}
