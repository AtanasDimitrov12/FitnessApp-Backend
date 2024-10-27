package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class TrainerEntityMapper {

    public TrainerEntityMapper(@Lazy WorkoutEntityMapper workoutEntityMapperImpl, DietEntityMapper dietEntityMapperImpl) {
        this.workoutEntityMapperImpl = workoutEntityMapperImpl;
        this.dietEntityMapperImpl = dietEntityMapperImpl;
    }

    private WorkoutEntityMapper workoutEntityMapperImpl;
    private DietEntityMapper dietEntityMapperImpl;


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
                trainerEntity.getWorkoutsCreated().stream().map(workoutEntityMapperImpl::toDomain).collect(Collectors.toList()),
                trainerEntity.getDietsCreated().stream().map(dietEntityMapperImpl::toDomain).collect(Collectors.toList())
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
        trainerEntity.setWorkoutsCreated(trainer.getWorkoutsCreated().stream().map(workoutEntityMapperImpl::toEntity).collect(Collectors.toList()));
        trainerEntity.setDietsCreated(trainer.getDietsCreated().stream().map(dietEntityMapperImpl::toEntity).collect(Collectors.toList()));

        return trainerEntity;
    }
}
