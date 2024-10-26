package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TrainerEntityMapper {

    private final WorkoutEntityMapper workoutEntityMapper;
    private final DietEntityMapper dietEntityMapper;

    public TrainerEntityMapper(WorkoutEntityMapper workoutEntityMapper, DietEntityMapper dietEntityMapper) {
        this.workoutEntityMapper = workoutEntityMapper;
        this.dietEntityMapper = dietEntityMapper;
    }

    public Trainer toDomain(TrainerEntity trainerEntity) {
        if (trainerEntity == null) {
            return null;
        }
        return new Trainer(
                trainerEntity.getId(),
                trainerEntity.getFirstName(),
                trainerEntity.getLastName(),
                trainerEntity.getUsername(),
                trainerEntity.getEmail(),
                trainerEntity.getAge(),
                trainerEntity.getGender(),
                trainerEntity.getExpertise(),
                trainerEntity.getPictureURL(),
                trainerEntity.getWorkoutsCreated().stream().map(workoutEntityMapper::toDomain).collect(Collectors.toList()),
                trainerEntity.getDietsCreated().stream().map(dietEntityMapper::toDomain).collect(Collectors.toList())
        );
    }

    public TrainerEntity toEntity(Trainer trainer) {
        if (trainer == null) {
            return null;
        }

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId(trainer.getId());
        trainerEntity.setFirstName(trainer.getFirstName());
        trainerEntity.setLastName(trainer.getLastName());
        trainerEntity.setUsername(trainer.getUsername());
        trainerEntity.setEmail(trainer.getEmail());
        trainerEntity.setAge(trainer.getAge());
        trainerEntity.setGender(trainer.getGender());
        trainerEntity.setExpertise(trainer.getExpertise());
        trainerEntity.setPictureURL(trainer.getPictureURL());
        trainerEntity.setWorkoutsCreated(trainer.getWorkoutsCreated().stream().map(workoutEntityMapper::toEntity).collect(Collectors.toList()));
        trainerEntity.setDietsCreated(trainer.getDietsCreated().stream().map(dietEntityMapper::toEntity).collect(Collectors.toList()));

        return trainerEntity;
    }
}
