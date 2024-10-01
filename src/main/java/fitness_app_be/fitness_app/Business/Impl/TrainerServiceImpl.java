package fitness_app_be.fitness_app.Business.Impl;

import fitness_app_be.fitness_app.Business.TrainerService;
import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.ExceptionHandling.TrainerNotFoundException;
import fitness_app_be.fitness_app.Persistence.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.getAll();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerRepository.getTrainerById(id)
                .orElseThrow(() -> new TrainerNotFoundException(id));
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.create(trainer);
    }

    @Override
    public void deleteTrainer(Long id) {
        if (!trainerRepository.exists(id)) {
            throw new TrainerNotFoundException(id);
        }
        trainerRepository.delete(id);
    }

    @Override
    public Trainer getTrainerByEmail(String email) {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email: " + email + " not found"));
    }

    @Override
    public List<Trainer> searchTrainersByPartialUsername(String partialUsername) {
        return trainerRepository.findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        Trainer existingTrainer = trainerRepository.findByEmail(trainer.getEmail())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email " + trainer.getEmail() + " not found"));


        existingTrainer.setFirstName(trainer.getFirstName());
        existingTrainer.setLastName(trainer.getLastName());
        existingTrainer.setUsername(trainer.getUsername());
        existingTrainer.setAge(trainer.getAge());
        existingTrainer.setGender(trainer.getGender());
        existingTrainer.setExpertise(trainer.getExpertise());

        return trainerRepository.update(existingTrainer);
    }
}
