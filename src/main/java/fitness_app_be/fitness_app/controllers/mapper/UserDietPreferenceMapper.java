package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.controllers.dto.UserDietPreferenceDTO;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDietPreferenceMapper {
    private final UserService userService;

    public UserDietPreference toDomain(UserDietPreferenceDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UserDietPreference(
                dto.getId(),
                userService.getUserById(dto.getUserId()),
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
                domain.getUser().getId(),
                domain.getCalories(),
                domain.getMealFrequency()
        );
    }
}
