package fitness_app_be.fitness_app.persistence.Impl.fake;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeTrainerRepositoryImpl implements TrainerRepository {

    private static long nextId = 1;
    private final List<Trainer> savedTrainers;

    public FakeTrainerRepositoryImpl() {
        this.savedTrainers = new ArrayList<>();
    }

    @Override
    public boolean exists(long id) {
        return savedTrainers.stream().anyMatch(trainer -> trainer.getId() == id);
    }

    @Override
    public List<Trainer> getAll() {
        return Collections.unmodifiableList(savedTrainers);
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setId(nextId++);
        savedTrainers.add(trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        Optional<Trainer> existingTrainer = getTrainerById(trainer.getId());
        if (existingTrainer.isPresent()) {
            Trainer foundTrainer = existingTrainer.get();
            foundTrainer.setFirstName(trainer.getFirstName());
            foundTrainer.setLastName(trainer.getLastName());
            foundTrainer.setEmail(trainer.getEmail());
            foundTrainer.setUsername(trainer.getUsername());
            foundTrainer.setExpertise(trainer.getExpertise());
            return foundTrainer;
        } else {
            throw new IllegalArgumentException("Trainer not found");
        }
    }

    @Override
    public void delete(long trainerId) {
        Optional<Trainer> existingTrainer = getTrainerById(trainerId);
        existingTrainer.ifPresent(savedTrainers::remove);
    }

    @Override
    public Optional<Trainer> getTrainerById(long trainerId) {
        return savedTrainers.stream().filter(trainer -> trainer.getId() == trainerId).findFirst();
    }

    @Override
    public Optional<Trainer> findByEmail(String email) {
        return savedTrainers.stream().filter(trainer -> trainer.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return savedTrainers.stream().filter(trainer -> trainer.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    @Override
    public List<Trainer> findByExpertise(String expertise) {
        List<Trainer> trainersWithExpertise = new ArrayList<>();
        for (Trainer trainer : savedTrainers) {
            if (trainer.getExpertise().equalsIgnoreCase(expertise)) {
                trainersWithExpertise.add(trainer);
            }
        }
        return trainersWithExpertise;
    }

    @Override
    public List<Trainer> findByUsernameContainingIgnoreCase(String partialUsername) {
        List<Trainer> matchingTrainers = new ArrayList<>();
        for (Trainer trainer : savedTrainers) {
            if (trainer.getUsername().toLowerCase().contains(partialUsername.toLowerCase())) {
                matchingTrainers.add(trainer);
            }
        }
        return matchingTrainers;
    }

    @Override
    public long countByEmail(String email) {
        return savedTrainers.stream().filter(trainer -> trainer.getEmail().equalsIgnoreCase(email)).count();
    }
}
