package fitness_app_be.fitness_app.MapperLayer;

import fitness_app_be.fitness_app.ControllerLayer.DTOs.TrainerDTO;
import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public TrainerDTO toDto(TrainerEntity trainerEntity) {
        return new TrainerDTO(trainerEntity.getId(), trainerEntity.getFirstName(),
                trainerEntity.getLastName(), trainerEntity.getEmail(),
                trainerEntity.getAge(), trainerEntity.getGender(),
                trainerEntity.getExpertise());
    }

    public TrainerEntity toEntity(TrainerDTO TrainerDTO) {
        return new TrainerEntity(TrainerDTO.getId(), TrainerDTO.getFirstName(),
                TrainerDTO.getLastName(), TrainerDTO.getEmail(), null,
                TrainerDTO.getAge(), TrainerDTO.getGender(),
                TrainerDTO.getExpertise());
    }

    public Trainer entityToDomain(TrainerEntity trainerEntity) {
        return new Trainer(trainerEntity.getId(), trainerEntity.getFirstName(),
                trainerEntity.getLastName(), trainerEntity.getEmail(),
                trainerEntity.getAge(), trainerEntity.getGender(),
                trainerEntity.getExpertise());
    }

    public TrainerEntity domainToEntity(Trainer trainer) {
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId(trainer.getId());
        trainerEntity.setFirstName(trainer.getFirstName());
        trainerEntity.setLastName(trainer.getLastName());
        trainerEntity.setEmail(trainer.getEmail());
        trainerEntity.setAge(trainer.getAge());
        trainerEntity.setGender(trainer.getGender());
        trainerEntity.setExpertise(trainer.getExpertise());
        return trainerEntity;
    }

    public Trainer toDomain(TrainerDTO TrainerDTO) {
        return new Trainer(TrainerDTO.getId(), TrainerDTO.getFirstName(),
                TrainerDTO.getLastName(), TrainerDTO.getEmail(),
                TrainerDTO.getAge(), TrainerDTO.getGender(),
                TrainerDTO.getExpertise());
    }

    public TrainerDTO domainToDto(Trainer trainer) {
        return new TrainerDTO(trainer.getId(), trainer.getFirstName(),
                trainer.getLastName(), trainer.getEmail(),
                trainer.getAge(), trainer.getGender(),
                trainer.getExpertise());
    }
}
