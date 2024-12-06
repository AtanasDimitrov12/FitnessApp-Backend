package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Role role;
    private WorkoutPlanDTO workoutPlan;
    private DietDTO diet;
    private List<ProgressNoteDTO> notes;

}
