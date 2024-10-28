package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.UserWorkoutPreferenceDTO;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import org.springframework.stereotype.Component;

@Component
public class UserWorkoutPreferenceMapper {

    public UserWorkoutPreference toDomain(UserWorkoutPreferenceDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UserWorkoutPreference(
                dto.getId(),
                dto.getUserid(),
                dto.getFitnessGoal(),
                dto.getFitnessLevel(),
                dto.getPreferredTrainingStyle(),
                dto.getDaysAvailable()
        );
    }

    public UserWorkoutPreferenceDTO toDto(UserWorkoutPreference domain) {
        if (domain == null) {
            return null;
        }

        return new UserWorkoutPreferenceDTO(
                domain.getId(),
                domain.getUserid(),
                domain.getFitnessGoal(),
                domain.getFitnessLevel(),
                domain.getPreferredTrainingStyle(),
                domain.getDaysAvailable()
        );
    }
}
