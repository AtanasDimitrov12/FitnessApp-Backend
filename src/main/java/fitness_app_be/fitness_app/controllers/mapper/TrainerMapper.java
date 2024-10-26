package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.TrainerDTO;
import fitness_app_be.fitness_app.domain.Trainer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TrainerMapper {

    private final WorkoutMapper workoutMapper;
    private final DietMapper dietMapper;

    public TrainerMapper(WorkoutMapper workoutMapper, DietMapper dietMapper) {
        this.workoutMapper = workoutMapper;
        this.dietMapper = dietMapper;
    }

    public Trainer toDomain(TrainerDTO trainerDTO) {
        return new Trainer(trainerDTO.getId(), trainerDTO.getFirstName(), trainerDTO.getLastName(),
                trainerDTO.getUsername(), trainerDTO.getEmail(), trainerDTO.getAge(),
                trainerDTO.getGender(), trainerDTO.getExpertise(), trainerDTO.getPictureURL(),
                trainerDTO.getWorkoutsCreated().stream().map(workoutMapper::toDomain).collect(Collectors.toList()),
                trainerDTO.getDietsCreated().stream().map(dietMapper::toDomain).collect(Collectors.toList()));
    }

    public TrainerDTO domainToDto(Trainer trainer) {
        return new TrainerDTO(trainer.getId(), trainer.getFirstName(), trainer.getLastName(),
                trainer.getUsername(), trainer.getEmail(), trainer.getAge(),
                trainer.getGender(), trainer.getExpertise(), trainer.getPictureURL(),
                trainer.getWorkoutsCreated().stream().map(workoutMapper::domainToDto).collect(Collectors.toList()),
                trainer.getDietsCreated().stream().map(dietMapper::domainToDto).collect(Collectors.toList()));
    }
}
