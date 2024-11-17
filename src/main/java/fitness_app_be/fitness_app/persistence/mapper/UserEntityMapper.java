package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserEntityMapper {

    private ProgressNoteEntityMapper progressNoteEntityMapperImpl;
    private UserDietPreferenceEntityMapper userDietPreferenceEntityMapper;
    private UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper;
    private WorkoutPlanEntityMapper workoutPlanEntityMapper;
    private DietEntityMapper dietEntityMapper;

    @Autowired
    public UserEntityMapper(@Lazy ProgressNoteEntityMapper progressNoteEntityMapperImpl,
                            @Lazy UserDietPreferenceEntityMapper userDietPreferenceEntityMapper,
                            @Lazy UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper,
                            @Lazy WorkoutPlanEntityMapper workoutPlanEntityMapper,
                            @Lazy DietEntityMapper dietEntityMapper) {
        this.progressNoteEntityMapperImpl = progressNoteEntityMapperImpl;
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
                .diets(userEntity.getDiets() != null
                        ? userEntity.getDiets().stream().map(dietEntityMapper::toDomain).toList()
                        : null)
                .notes(userEntity.getNotes() != null
                        ? userEntity.getNotes().stream().map(progressNoteEntityMapperImpl::toDomain).toList()
                        : null)
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

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setPictureURL(user.getPictureURL());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());
        userEntity.setIsActive(user.getIsActive());
        userEntity.setRole(user.getRole());

        // Map diet preference and workout preference
        userEntity.setDietPreference(userDietPreferenceEntityMapper.toEntity(user.getDietPreference(), userEntity));
        userEntity.setWorkoutPreference(userWorkoutPreferenceEntityMapper.toEntity(user.getWorkoutPreference(), userEntity));

        // Map workout plan
        userEntity.setWorkoutPlan(workoutPlanEntityMapper.toEntity(user.getWorkoutPlan()));

        // Map diets
        if (user.getDiets() != null) {
            userEntity.setDiets(
                    user.getDiets().stream()
                            .map(dietEntityMapper::toEntity)
                            .toList()
            );
        }

        // Map progress notes
        if (user.getNotes() != null) {
            userEntity.setNotes(
                    user.getNotes().stream()
                            .map(progressNoteEntityMapperImpl::toEntity)
                            .toList()
            );
        }

        return userEntity;
    }
}
