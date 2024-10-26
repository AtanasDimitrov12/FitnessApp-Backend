package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.domain.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final WorkoutMapper workoutMapper;
    private final DietMapper dietMapper;
    private final ProgressNoteMapper progressNoteMapper;

    public UserMapper(WorkoutMapper workoutMapper, DietMapper dietMapper, ProgressNoteMapper progressNoteMapper) {
        this.workoutMapper = workoutMapper;
        this.dietMapper = dietMapper;
        this.progressNoteMapper = progressNoteMapper;
    }

    public User toDomain(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                userDTO.getFitnessGoal(), userDTO.getDietPreference(), userDTO.getPictureURL(),
                userDTO.getWorkouts().stream().map(workoutMapper::toDomain).collect(Collectors.toList()),
                userDTO.getDiets().stream().map(dietMapper::toDomain).collect(Collectors.toList()),
                userDTO.getNotes().stream().map(progressNoteMapper::toDomain).collect(Collectors.toList()));
    }

    public UserDTO domainToDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getFitnessGoal(), user.getDietPreference(), user.getPictureURL(),
                user.getWorkouts().stream().map(workoutMapper::domainToDto).collect(Collectors.toList()),
                user.getDiets().stream().map(dietMapper::domainToDto).collect(Collectors.toList()),
                user.getNotes().stream().map(progressNoteMapper::domainToDto).collect(Collectors.toList()));
    }
}
