package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@NoArgsConstructor
public class UserEntityMapper {

    private ProgressNoteEntityMapper progressNoteEntityMapper;
    private UserDietPreferenceEntityMapper userDietPreferenceEntityMapper;
    private UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper;
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;
    private DietEntityMapper dietEntityMapper;

    @Autowired
    public UserEntityMapper(@Lazy ProgressNoteEntityMapper progressNoteEntityMapper,
                            @Lazy UserDietPreferenceEntityMapper userDietPreferenceEntityMapper,
                            @Lazy UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper,
                            @Lazy WorkoutPlanEntityMapper workoutPlanEntityMapper,
                            @Lazy DietEntityMapper dietEntityMapper) {
        this.progressNoteEntityMapper = progressNoteEntityMapper;
        this.userDietPreferenceEntityMapper = userDietPreferenceEntityMapper;
        this.userWorkoutPreferenceEntityMapper = userWorkoutPreferenceEntityMapper;
        this.workoutPlanEntityMapper = workoutPlanEntityMapper;
        this.dietEntityMapper = dietEntityMapper;
    }

    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .dietPreference(userDietPreferenceEntityMapper.toDomain(userEntity.getDietPreference()))
                .workoutPreference(userWorkoutPreferenceEntityMapper.toDomain(userEntity.getWorkoutPreference()))
                .pictureURL(userEntity.getPictureURL())
                .workoutPlan(workoutPlanEntityMapper.toDomain(userEntity.getWorkoutPlan()))
                .diet(userEntity.getDiet() != null ? dietEntityMapper.toDomain(userEntity.getDiet()) : null)
                .notes(userEntity.getNotes() != null
                        ? userEntity.getNotes().stream().map(progressNoteEntityMapper::toDomain).toList()
                        : Collections.emptyList())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .isActive(userEntity.getIsActive())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity =  new UserEntity();

        // Set fields that should always be updated
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPictureURL(user.getPictureURL());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());
        userEntity.setIsActive(user.getIsActive());
        userEntity.setRole(user.getRole());

        // Update password only if provided
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userEntity.setPassword(user.getPassword());
        }

        // Map diet preference and workout preference
        if (user.getDietPreference() != null) {
            userEntity.setDietPreference(userDietPreferenceEntityMapper.toEntity(user.getDietPreference()));
        }
        if (user.getWorkoutPreference() != null) {
            userEntity.setWorkoutPreference(userWorkoutPreferenceEntityMapper.toEntity(user.getWorkoutPreference(), userEntity));
        }

        // Map workout plan
        if (user.getWorkoutPlan() != null) {
            userEntity.setWorkoutPlan(workoutPlanEntityMapper.toEntity(user.getWorkoutPlan()));
        }

        // Map diet
        if (user.getDiet() != null) {
            userEntity.setDiet(dietEntityMapper.toEntity(user.getDiet()));
        }

        // Map progress notes
        if (user.getNotes() != null) {
            userEntity.setNotes(
                    user.getNotes().stream()
                            .map(progressNoteEntityMapper::toEntity)
                            .toList()
            );
        }

        return userEntity;
    }

}
