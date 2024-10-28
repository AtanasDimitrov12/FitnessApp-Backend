package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserEntityMapper {

    private ProgressNoteEntityMapper progressNoteEntityMapperImpl;
    private UserDietPreferenceEntityMapper userDietPreferenceEntityMapper;
    private UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper;

    @Autowired
    public UserEntityMapper(@Lazy ProgressNoteEntityMapper progressNoteEntityMapperImpl,
                            @Lazy UserDietPreferenceEntityMapper userDietPreferenceEntityMapper,
                            @Lazy UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper) {
        this.progressNoteEntityMapperImpl = progressNoteEntityMapperImpl;
        this.userDietPreferenceEntityMapper = userDietPreferenceEntityMapper;
        this.userWorkoutPreferenceEntityMapper = userWorkoutPreferenceEntityMapper;
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
                .workoutPlanId(userEntity.getWorkoutPlanId())
                .dietId(userEntity.getDietId())
                .notes(userEntity.getNotes() != null
                        ? userEntity.getNotes().stream().map(progressNoteEntityMapperImpl::toDomain).collect(Collectors.toList())
                        : null)
                .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                null, // Placeholder for dietPreference, to be set below
                null, // Placeholder for workoutPreference, to be set below
                user.getPictureURL(),
                user.getWorkoutPlanId(),
                user.getDietId(),
                user.getNotes() != null
                        ? user.getNotes().stream().map(progressNoteEntityMapperImpl::toEntity).collect(Collectors.toList())
                        : null
        );

        userEntity.setDietPreference(userDietPreferenceEntityMapper.toEntity(user.getDietPreference(), userEntity));
        userEntity.setWorkoutPreference(userWorkoutPreferenceEntityMapper.toEntity(user.getWorkoutPreference(), userEntity));

        return userEntity;
    }
}
