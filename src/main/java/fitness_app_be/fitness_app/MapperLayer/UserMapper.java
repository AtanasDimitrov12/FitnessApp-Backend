package fitness_app_be.fitness_app.MapperLayer;

import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getFitnessGoal(), userEntity.getDietPreference());
    }

    public UserEntity toEntity(UserDTO userDTO) {
        return new UserEntity(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(),
                null,
                userDTO.getFitnessGoal(), userDTO.getDietPreference());
    }
}
