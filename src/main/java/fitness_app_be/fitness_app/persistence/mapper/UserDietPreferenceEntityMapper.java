package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserDietPreferenceEntityMapper {

    public UserDietPreference toDomain(UserDietPreferenceEntity userDietPreferenceEntity) {
        if (userDietPreferenceEntity == null) {
            return null;
        }

        return new UserDietPreference(
                userDietPreferenceEntity.getId(),
                userDietPreferenceEntity.getUser() != null ? userDietPreferenceEntity.getUser().getId() : null,
                userDietPreferenceEntity.getCalories(),
                userDietPreferenceEntity.getMealFrequency()
        );
    }

    public UserDietPreferenceEntity toEntity(UserDietPreference userDietPreference, UserEntity userEntity) {
        if (userDietPreference == null) {
            return null;
        }

        return new UserDietPreferenceEntity(
                userDietPreference.getId(),
                userEntity,
                userDietPreference.getCalories(),
                userDietPreference.getMealFrequency()
        );
    }
}
