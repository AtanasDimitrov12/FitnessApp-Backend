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

    public UserMapper( ProgressNoteMapper progressNoteMapper) {
        this.progressNoteMapper = progressNoteMapper;
    }


    public User toDomain(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(),
                userDTO.getFitnessGoal(), userDTO.getDietPreference(), userDTO.getPictureURL(),
                userDTO.getWorkoutPlanId(), userDTO.getDietId(),
                userDTO.getNotes().stream().map(progressNoteMapper::toDomain).collect(Collectors.toList()));
    }

    public UserDTO domainToDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.getFitnessGoal(), user.getDietPreference(), user.getPictureURL(),
                user.getWorkoutPlanId(), user.getDietId(),
                user.getNotes().stream().map(progressNoteMapper::domainToDto).collect(Collectors.toList()));
    }
}
