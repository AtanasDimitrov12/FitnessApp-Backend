package fitness_app_be.fitness_app.Business;

import fitness_app_be.fitness_app.Domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long id);
    User createUser(User user);
    void deleteUser(long userId);
    Optional<User> getUserByEmail(String email);
    List<User> searchUsersByPartialUsername(String partialUsername);
    User updateUser(User user);
}
