package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ProgressNoteMapper progressNoteMapper;
    private final UserDietPreferenceMapper userDietPreferenceMapper;
    private final UserWorkoutPreferenceMapper userWorkoutPreferenceMapper;
    private final DietMapper dietMapper;
    private final WorkoutPlanMapper workoutPlanMapper;

    @Autowired
    public UserMapper(@Lazy ProgressNoteMapper progressNoteMapper,
                      @Lazy UserDietPreferenceMapper userDietPreferenceMapper,
                      @Lazy UserWorkoutPreferenceMapper userWorkoutPreferenceMapper,
                      @Lazy DietMapper dietMapper,
                      @Lazy WorkoutPlanMapper workoutPlanMapper) {
        this.progressNoteMapper = progressNoteMapper;
        this.userDietPreferenceMapper = userDietPreferenceMapper;
        this.userWorkoutPreferenceMapper = userWorkoutPreferenceMapper;
        this.dietMapper = dietMapper;
        this.workoutPlanMapper = workoutPlanMapper;
    }

    public User toDomain(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .dietPreference(userDietPreferenceMapper.toDomain(userDTO.getDietPreference()))
                .workoutPreference(userWorkoutPreferenceMapper.toDomain(userDTO.getWorkoutPreference()))
                .pictureURL(userDTO.getPictureURL())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .role(userDTO.getRole())
                .workoutPlan(workoutPlanMapper.toDomain(userDTO.getWorkoutPlan()))
                .diets(userDTO.getDiets() != null
                        ? userDTO.getDiets().stream()
                        .map(dietMapper::toDomain)
                        .toList()
                        : null)
                .notes(userDTO.getNotes() != null
                        ? userDTO.getNotes().stream()
                        .map(progressNoteMapper::toDomain)
                        .toList()
                        : null)
                .isActive(userDTO.getIsActive())
                .build();
    }

    public UserDTO domainToDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .dietPreference(userDietPreferenceMapper.toDto(user.getDietPreference()))
                .workoutPreference(userWorkoutPreferenceMapper.toDto(user.getWorkoutPreference()))
                .pictureURL(user.getPictureURL())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(user.getRole())
                .workoutPlan(workoutPlanMapper.domainToDto(user.getWorkoutPlan()))
                .diets(user.getDiets() != null
                        ? user.getDiets().stream()
                        .map(dietMapper::domainToDto)
                        .toList()
                        : null)
                .notes(user.getNotes() != null
                        ? user.getNotes().stream()
                        .map(progressNoteMapper::domainToDto)
                        .toList()
                        : null)
                .isActive(user.getIsActive())
                .build();
    }
}
