package fitness_app_be.fitness_app.persistence.JPARepositories;

import fitness_app_be.fitness_app.persistence.Entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaTrainerRepository extends JpaRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByEmail(String email);

    Optional<TrainerEntity> findByUsername(String username);

    List<TrainerEntity> findByExpertiseContainingIgnoreCase(String expertise);

    List<TrainerEntity> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
