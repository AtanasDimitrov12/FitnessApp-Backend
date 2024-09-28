package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {

    // Find a trainer by their email address
    Optional<TrainerEntity> findByEmail(String email);

    // Find a trainer by their username
    Optional<TrainerEntity> findByUsername(String username);  // Corrected to use lowercase 'username'

    // Find trainers by their fitness goal (expertise)
    List<TrainerEntity> findByExpertise(String expertise);

    // Find trainers whose username contains a specific string (case-insensitive)
    List<TrainerEntity> findByUsernameContainingIgnoreCase(String partialUsername);

    // Count trainers by their email address (for uniqueness checks)
    long countByEmail(String email);
}
