package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserDietPreference dietPreference;
    private UserWorkoutPreference workoutPreference;
    private String pictureURL;
    private Long workoutPlanId;
    private Long dietId;
    private List<ProgressNote> notes;
}
