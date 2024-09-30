package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByFitnessGoal(String fitnessGoal);

    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
