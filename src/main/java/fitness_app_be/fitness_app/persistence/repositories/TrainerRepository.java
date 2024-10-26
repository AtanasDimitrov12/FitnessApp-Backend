package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Trainer;

import java.util.Optional;
import java.util.List;

public interface TrainerRepository {

    boolean exists(long id);

    List<Trainer> getAll();

    Trainer create(Trainer trainer);

    Trainer update(Trainer trainer);

    void delete(long trainerId);

    Optional<Trainer> getTrainerById(long trainerId);

    Optional<Trainer> findByEmail(String email);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findByExpertise(String expertise);

    List<Trainer> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
