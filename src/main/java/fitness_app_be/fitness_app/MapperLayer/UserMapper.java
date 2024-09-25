package fitness_app_be.fitness_app.MapperLayer;

import fitness_app_be.fitness_app.ControllerLayer.DTOs.UserDTO;
import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Mapping UserEntity to UserDTO
    public UserDTO toDto(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getFitnessGoal(), userEntity.getDietPreference());
    }

    // Mapping UserDTO to UserEntity
    public UserEntity toEntity(UserDTO userDTO) {
        return new UserEntity(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                null, userDTO.getFitnessGoal(), userDTO.getDietPreference());
    }

    // Mapping UserEntity to Domain User
    public User entityToDomain(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getFitnessGoal(), userEntity.getDietPreference());
    }

    // Mapping Domain User to UserEntity
    public UserEntity domainToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setFitnessGoal(user.getFitnessGoal());
        userEntity.setDietPreference(user.getDietPreference());
        return userEntity;
    }

    // Mapping Domain User to UserDTO
    public User toDomain(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                userDTO.getFitnessGoal(), userDTO.getDietPreference());
    }

    // Mapping UserDTO to Domain User
    public UserDTO domainToDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getFitnessGoal(), user.getDietPreference());
    }
}
