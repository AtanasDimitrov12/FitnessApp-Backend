package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.UserDietPreferenceDTO;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import org.springframework.stereotype.Component;

@Component
public class UserDietPreferenceMapper {

    public UserDietPreference toDomain(UserDietPreferenceDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UserDietPreference(
                dto.getId(),
                dto.getUser(),
                dto.getCalories(),
                dto.getMealFrequency()
        );
    }

    public UserDietPreferenceDTO toDto(UserDietPreference domain) {
        if (domain == null) {
            return null;
        }

        return new UserDietPreferenceDTO(
                domain.getId(),
                domain.getUser(),
                domain.getCalories(),
                domain.getMealFrequency()
        );
    }
}
