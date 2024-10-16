package fitness_app_be.fitness_app.persistence.Mapper;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.Entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerEntityMapper {

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
                trainerEntity.getPictureURL()
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

        return trainerEntity;
    }
}

