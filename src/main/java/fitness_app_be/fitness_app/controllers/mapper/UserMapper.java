package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ProgressNoteMapper progressNoteMapper;
    private final UserDietPreferenceMapper userDietPreferenceMapper;
    private final UserWorkoutPreferenceMapper userWorkoutPreferenceMapper;

    @Autowired
    public UserMapper(@Lazy ProgressNoteMapper progressNoteMapper,
                      @Lazy UserDietPreferenceMapper userDietPreferenceMapper,
                      @Lazy UserWorkoutPreferenceMapper userWorkoutPreferenceMapper) {
        this.progressNoteMapper = progressNoteMapper;
        this.userDietPreferenceMapper = userDietPreferenceMapper;
        this.userWorkoutPreferenceMapper = userWorkoutPreferenceMapper;
    }

    public User toDomain(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDietPreferenceMapper.toDomain(userDTO.getDietPreference()),
                userWorkoutPreferenceMapper.toDomain(userDTO.getWorkoutPreference()),
                userDTO.getPictureURL(),
                userDTO.getWorkoutPlanId(),
                userDTO.getDietId(),
                userDTO.getNotes() != null
                        ? userDTO.getNotes().stream()
                        .map(progressNoteMapper::toDomain)
                        .collect(Collectors.toList())
                        : null
        );
    }

    public UserDTO domainToDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                userDietPreferenceMapper.toDto(user.getDietPreference()),
                userWorkoutPreferenceMapper.toDto(user.getWorkoutPreference()),
                user.getPictureURL(),
                user.getWorkoutPlanId(),
                user.getDietId(),
                user.getNotes() != null
                        ? user.getNotes().stream()
                        .map(progressNoteMapper::domainToDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
