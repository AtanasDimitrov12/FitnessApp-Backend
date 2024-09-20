package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByFitnessGoal(String fitnessGoal);

    List<UserEntity> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
