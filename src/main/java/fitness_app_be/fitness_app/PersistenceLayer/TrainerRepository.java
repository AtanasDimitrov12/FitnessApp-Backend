package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.Domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByEmail(String email);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findByExpertise(String expertise);

    List<Trainer> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
