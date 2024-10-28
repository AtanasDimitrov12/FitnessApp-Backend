package fitness_app_be.fitness_app.controllers.dto;

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
    private String fitnessGoal;
    private String dietPreference;
    private String pictureURL;
    private Long workoutPlanId;
    private Long dietId;
    private List<ProgressNoteDTO> notes;
}

