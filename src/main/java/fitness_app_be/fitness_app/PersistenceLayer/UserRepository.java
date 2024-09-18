package fitness_app_be.fitness_app.PersistenceLayer;

import fitness_app_be.fitness_app.PersistenceLayer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email
    Optional<User> findByEmail(String email);

    // Find a user by their username
    Optional<User> findByUsername(String username);

    // Find all users with a specific fitness goal (example method for extra flexibility)
    List<User> findByFitnessGoal(String fitnessGoal);

    // Find users by partial match on username (useful for search scenarios)
    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    // Count the number of users with a specific email (useful to check if email already exists)
    long countByEmail(String email);
}
