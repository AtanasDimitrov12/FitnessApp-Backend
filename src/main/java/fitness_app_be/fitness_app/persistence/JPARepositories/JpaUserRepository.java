package fitness_app_be.fitness_app.persistence.JPARepositories;

import fitness_app_be.fitness_app.persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByFitnessGoal(String fitnessGoal);
    List<UserEntity> findByUsernameContainingIgnoreCase(String partialUsername);
    long countByEmail(String email);
}

