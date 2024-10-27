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

    private WorkoutEntityMapper workoutEntityMapperImpl;
    private DietEntityMapper dietEntityMapperImpl;

    public UserEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapperImpl, DietEntityMapper dietEntityMapperImpl, ProgressNoteEntityMapper progressNoteEntityMapperImpl) {
        this.workoutEntityMapperImpl = workoutEntityMapperImpl;
        this.dietEntityMapperImpl = dietEntityMapperImpl;
        this.progressNoteEntityMapperImpl = progressNoteEntityMapperImpl;
    }

    private ProgressNoteEntityMapper progressNoteEntityMapperImpl;



    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFitnessGoal(),
                userEntity.getDietPreference(),
                userEntity.getPictureURL(),
                userEntity.getWorkouts().stream().map(workoutEntityMapperImpl::toDomain).collect(Collectors.toList()),
                userEntity.getDiets().stream().map(dietEntityMapperImpl::toDomain).collect(Collectors.toList()),
                userEntity.getProgressNotes().stream().map(progressNoteEntityMapperImpl::toDomain).collect(Collectors.toList())
        );
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setFitnessGoal(user.getFitnessGoal());
        userEntity.setDietPreference(user.getDietPreference());
        userEntity.setPictureURL(user.getPictureURL());

        userEntity.setWorkouts(user.getWorkouts().stream().map(workoutEntityMapperImpl::toEntity).collect(Collectors.toList()));
        userEntity.setDiets(user.getDiets().stream().map(dietEntityMapperImpl::toEntity).collect(Collectors.toList()));
        userEntity.setProgressNotes(user.getNotes().stream().map(progressNoteEntityMapperImpl::toEntity).collect(Collectors.toList()));

        return userEntity;
    }
}
