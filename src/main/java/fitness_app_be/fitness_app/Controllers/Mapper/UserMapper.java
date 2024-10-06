package fitness_app_be.fitness_app.Controllers.Mapper;

import fitness_app_be.fitness_app.Controllers.DTOs.UserDTO;
import fitness_app_be.fitness_app.Domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {



    public User toDomain(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                userDTO.getFitnessGoal(), userDTO.getDietPreference(), userDTO.getPictureURL());
    }


    public UserDTO domainToDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getFitnessGoal(), user.getDietPreference(), user.getPictureURL());
    }

}
