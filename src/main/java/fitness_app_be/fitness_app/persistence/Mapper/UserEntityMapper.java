package fitness_app_be.fitness_app.persistence.Mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFitnessGoal(),
                entity.getDietPreference(),
                entity.getPictureURL()
        );
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setFitnessGoal(user.getFitnessGoal());
        entity.setDietPreference(user.getDietPreference());
        return entity;
    }
}

