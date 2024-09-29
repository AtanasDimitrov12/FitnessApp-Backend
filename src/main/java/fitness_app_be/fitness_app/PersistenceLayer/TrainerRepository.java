package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {

    Optional<TrainerEntity> findByEmail(String email);

    Optional<TrainerEntity> findByUsername(String username);  // Corrected to use lowercase 'username'

    List<TrainerEntity> findByExpertise(String expertise);

    List<TrainerEntity> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
