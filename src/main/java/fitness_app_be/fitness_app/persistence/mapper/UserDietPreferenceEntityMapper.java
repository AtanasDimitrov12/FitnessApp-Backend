package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDietPreferenceEntityMapper {

    private final UserEntityMapper userEntityMapper;

    public UserDietPreference toDomain(UserDietPreferenceEntity userDietPreferenceEntity) {
        if (userDietPreferenceEntity == null) {
            return null;
        }

        return new UserDietPreference(
                userDietPreferenceEntity.getId(),
                userDietPreferenceEntity.getUserId(),
                userDietPreferenceEntity.getCalories(),
                userDietPreferenceEntity.getMealFrequency()
        );
    }

    public UserDietPreferenceEntity toEntity(UserDietPreference userDietPreference) {
        if (userDietPreference == null) {
            return null;
        }

        return new UserDietPreferenceEntity(
                userDietPreference.getId(),
                userDietPreference.getUserId(),
                userDietPreference.getCalories(),
                userDietPreference.getMealFrequency()
        );
    }
}
