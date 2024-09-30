package fitness_app_be.fitness_app.Controllers.Mapper;

import fitness_app_be.fitness_app.Controllers.DTOs.TrainerDTO;
import fitness_app_be.fitness_app.Domain.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {



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
