package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserEntityMapper{

    private ProgressNoteEntityMapper progressNoteEntityMapperImpl;

    @Autowired
    public UserEntityMapper( ProgressNoteEntityMapper progressNoteEntityMapperImpl) {
        this.progressNoteEntityMapperImpl = progressNoteEntityMapperImpl;
    }




    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getFitnessGoal(),
                userEntity.getDietPreference(),
                userEntity.getPictureURL(),
                userEntity.getWorkoutPlanId(),
                userEntity.getDietId(),
                userEntity.getNotes().stream().map(progressNoteEntityMapperImpl::toDomain).collect(Collectors.toList())
        );
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFitnessGoal(),
                user.getDietPreference(),
                user.getPictureURL(),
                user.getWorkoutPlanId(),
                user.getDietId(),
                user.getNotes().stream().map(progressNoteEntityMapperImpl::toEntity).collect(Collectors.toList()));



    }
}
