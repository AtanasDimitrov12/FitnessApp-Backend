package fitness_app_be.fitness_app.BusinessLayer;

import fitness_app_be.fitness_app.Domain.Trainer;

import java.util.List;

public interface TrainerService {
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    Trainer createTrainer(Trainer trainer);
    void deleteTrainer(Long id);
    Trainer getTrainerByEmail(String email);
    List<Trainer> searchTrainersByPartialUsername(String partialUsername);
    Trainer updateTrainer(Trainer trainer);
}
