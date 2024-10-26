package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserEntityMapper {

    private final WorkoutEntityMapper workoutEntityMapper;
    private final DietEntityMapper dietEntityMapper;
    private final ProgressNoteEntityMapper progressNoteEntityMapper;

    public UserEntityMapper(WorkoutEntityMapper workoutEntityMapper, DietEntityMapper dietEntityMapper, ProgressNoteEntityMapper progressNoteEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
        this.dietEntityMapper = dietEntityMapper;
        this.progressNoteEntityMapper = progressNoteEntityMapper;
    }

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
                userEntity.getWorkouts().stream().map(workoutEntityMapper::toDomain).collect(Collectors.toList()),
                userEntity.getDiets().stream().map(dietEntityMapper::toDomain).collect(Collectors.toList()),
                userEntity.getProgressNotes().stream().map(progressNoteEntityMapper::toDomain).collect(Collectors.toList())
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

        userEntity.setWorkouts(user.getWorkouts().stream().map(workoutEntityMapper::toEntity).collect(Collectors.toList()));
        userEntity.setDiets(user.getDiets().stream().map(dietEntityMapper::toEntity).collect(Collectors.toList()));
        userEntity.setProgressNotes(user.getNotes().stream().map(progressNoteEntityMapper::toEntity).collect(Collectors.toList()));

        return userEntity;
    }
}
