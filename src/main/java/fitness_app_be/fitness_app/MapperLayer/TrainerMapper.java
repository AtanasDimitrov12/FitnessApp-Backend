package fitness_app_be.fitness_app.MapperLayer;

import fitness_app_be.fitness_app.ControllerLayer.DTOs.TrainerDTO;
import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public TrainerDTO toDto(TrainerEntity trainerEntity) {
        return new TrainerDTO(trainerEntity.getId(), trainerEntity.getFirstName(),
                trainerEntity.getLastName(), trainerEntity.getUsername(),  // Added username
                trainerEntity.getEmail(), trainerEntity.getAge(),
                trainerEntity.getGender(), trainerEntity.getExpertise());
    }

    public TrainerEntity toEntity(TrainerDTO trainerDTO) {
        return new TrainerEntity(trainerDTO.getId(), trainerDTO.getFirstName(),
                trainerDTO.getLastName(), trainerDTO.getUsername(),  // Added username
                trainerDTO.getEmail(), null, trainerDTO.getAge(),
                trainerDTO.getGender(), trainerDTO.getExpertise());
    }

    public Trainer entityToDomain(TrainerEntity trainerEntity) {
        return new Trainer(trainerEntity.getId(), trainerEntity.getFirstName(),
                trainerEntity.getLastName(), trainerEntity.getUsername(),  // Added username
                trainerEntity.getEmail(), trainerEntity.getAge(),
                trainerEntity.getGender(), trainerEntity.getExpertise());
    }

    public TrainerEntity domainToEntity(Trainer trainer) {
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId(trainer.getId());
        trainerEntity.setFirstName(trainer.getFirstName());
        trainerEntity.setLastName(trainer.getLastName());
        trainerEntity.setUsername(trainer.getUsername());  // Added username
        trainerEntity.setEmail(trainer.getEmail());
        trainerEntity.setAge(trainer.getAge());
        trainerEntity.setGender(trainer.getGender());
        trainerEntity.setExpertise(trainer.getExpertise());
        return trainerEntity;
    }

    public Trainer toDomain(TrainerDTO trainerDTO) {
        return new Trainer(trainerDTO.getId(), trainerDTO.getFirstName(),
                trainerDTO.getLastName(), trainerDTO.getUsername(),  // Added username
                trainerDTO.getEmail(), trainerDTO.getAge(),
                trainerDTO.getGender(), trainerDTO.getExpertise());
    }

    public TrainerDTO domainToDto(Trainer trainer) {
        return new TrainerDTO(trainer.getId(), trainer.getFirstName(),
                trainer.getLastName(), trainer.getUsername(),  // Added username
                trainer.getEmail(), trainer.getAge(),
                trainer.getGender(), trainer.getExpertise());
    }
}
