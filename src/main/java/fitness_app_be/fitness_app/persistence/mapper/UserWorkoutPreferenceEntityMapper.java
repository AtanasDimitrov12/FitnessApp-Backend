package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.persistence.entity.UserWorkoutPreferenceEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserWorkoutPreferenceEntityMapper {


    public UserWorkoutPreference toDomain(UserWorkoutPreferenceEntity userWorkoutPreferenceEntity) {
        if (userWorkoutPreferenceEntity == null) {
            return null;
        }

        return new UserWorkoutPreference(
                userWorkoutPreferenceEntity.getId(),
                userWorkoutPreferenceEntity.getUser() != null ? userWorkoutPreferenceEntity.getUser().getId() : null,
                userWorkoutPreferenceEntity.getFitnessGoal(),
                userWorkoutPreferenceEntity.getFitnessLevel(),
                userWorkoutPreferenceEntity.getPreferredTrainingStyle(),
                userWorkoutPreferenceEntity.getDaysAvailable()
        );
    }

    public UserWorkoutPreferenceEntity toEntity(UserWorkoutPreference userWorkoutPreference, UserEntity userEntity) {
        if (userWorkoutPreference == null) {
            return null;
        }

        return new UserWorkoutPreferenceEntity(
                userWorkoutPreference.getId(),
                userEntity,
                userWorkoutPreference.getFitnessGoal(),
                userWorkoutPreference.getFitnessLevel(),
                userWorkoutPreference.getPreferredTrainingStyle(),
                userWorkoutPreference.getDaysAvailable()
        );
    }
}
