package fitness_app_be.fitness_app.MapperLayer;

import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getFitnessGoal(), user.getDietPreference());
    }

    public User toEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                null, // We don't want to transfer the password via DTO
                userDTO.getFitnessGoal(), userDTO.getDietPreference());
    }
}
