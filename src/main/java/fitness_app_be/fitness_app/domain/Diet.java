package fitness_app_be.fitness_app.domain;


import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diet {

    @Setter(AccessLevel.NONE)
    private Long id;
    private User user;
    private List<Meal> meals;
}
