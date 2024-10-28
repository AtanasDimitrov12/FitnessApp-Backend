package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserDietPreferenceDTO dietPreference;
    private UserWorkoutPreferenceDTO workoutPreference;
    private String pictureURL;
    private Long workoutPlanId;
    private Long dietId;
    private List<ProgressNoteDTO> notes;
}

