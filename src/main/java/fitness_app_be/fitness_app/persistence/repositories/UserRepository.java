package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean exists(long id);

    List<User> getAll();

    List<User> getAll(int pageNumber, int pageSize); // Optional pagination

    User create(User user);

    User update(User user);

    void delete(long userId);

    void deactivateUser(long userId); // Soft delete option

    Optional<User> getUserById(long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndIsActive(String username, boolean isActive);

    Optional<User> findByEmailAndIsActive(String email, boolean isActive);

    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    List<User> findByIsActive(boolean isActive); // Filter by active status

    long countByEmail(String email);

    long countByIsActive(boolean isActive); // Count active/inactive users

    UserEntity findEntityById(long userId);

    List<Long> getUsersWithWorkout(Long workoutId);
}
