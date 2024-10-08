package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.TrainerDTO;
import fitness_app_be.fitness_app.domain.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {



    public Trainer toDomain(TrainerDTO trainerDTO) {
        return new Trainer(trainerDTO.getId(), trainerDTO.getFirstName(),
                trainerDTO.getLastName(), trainerDTO.getUsername(),
                trainerDTO.getEmail(), trainerDTO.getAge(),
                trainerDTO.getGender(), trainerDTO.getExpertise(), trainerDTO.getPictureURL());
    }

    public TrainerDTO domainToDto(Trainer trainer) {
        return new TrainerDTO(trainer.getId(), trainer.getFirstName(),
                trainer.getLastName(), trainer.getUsername(),
                trainer.getEmail(), trainer.getAge(),
                trainer.getGender(), trainer.getExpertise(), trainer.getPictureURL());
    }
}