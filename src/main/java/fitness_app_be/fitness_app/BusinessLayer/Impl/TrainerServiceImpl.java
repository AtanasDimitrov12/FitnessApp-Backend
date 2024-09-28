package fitness_app_be.fitness_app.BusinessLayer.Impl;

import fitness_app_be.fitness_app.BusinessLayer.TrainerService;
import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.TrainerNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.TrainerMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import fitness_app_be.fitness_app.PersistenceLayer.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll().stream()
                .map(trainerMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .map(trainerMapper::entityToDomain)
                .orElseThrow(() -> new TrainerNotFoundException(id));
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        TrainerEntity newEntity = trainerMapper.domainToEntity(trainer);
        TrainerEntity savedTrainerEntity = trainerRepository.save(newEntity);
        return trainerMapper.entityToDomain(savedTrainerEntity);
    }

    @Override
    public void deleteTrainer(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new TrainerNotFoundException(id);
        }
        trainerRepository.deleteById(id);
    }

    @Override
    public Trainer getTrainerByEmail(String email) {
        return trainerRepository.findByEmail(email)
                .map(trainerMapper::entityToDomain)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email: " + email + " not found"));
    }

    @Override
    public List<Trainer> searchTrainersByPartialUsername(String partialUsername) {
        return trainerRepository.findByUsernameContainingIgnoreCase(partialUsername).stream()
                .map(trainerMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        TrainerEntity existingTrainerEntity = trainerRepository.findByEmail(trainer.getEmail())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email " + trainer.getEmail() + " not found"));

        // Update all necessary fields
        existingTrainerEntity.setFirstName(trainer.getFirstName());
        existingTrainerEntity.setLastName(trainer.getLastName());
        existingTrainerEntity.setUsername(trainer.getUsername());
        existingTrainerEntity.setAge(trainer.getAge());
        existingTrainerEntity.setGender(trainer.getGender());
        existingTrainerEntity.setExpertise(trainer.getExpertise());

        TrainerEntity updatedTrainerEntity = trainerRepository.save(existingTrainerEntity);
        return trainerMapper.entityToDomain(updatedTrainerEntity);
    }
}
