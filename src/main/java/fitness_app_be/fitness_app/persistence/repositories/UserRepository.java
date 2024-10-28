package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository {

    boolean exists(long id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(long userId);

    Optional<User> getUserById(long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    long countByEmail(String email);
}
