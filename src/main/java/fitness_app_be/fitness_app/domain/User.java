package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.util.ArrayList;
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
    private WorkoutPlan workoutPlan;
    private List<Diet> diets = new ArrayList<>();
    private List<ProgressNote> notes = new ArrayList<>();
}
