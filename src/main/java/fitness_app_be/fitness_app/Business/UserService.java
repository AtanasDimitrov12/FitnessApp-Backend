package fitness_app_be.fitness_app.Business;

import fitness_app_be.fitness_app.Domain.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(User user);
    void deleteUser(Long id);
    User getUserByEmail(String email);
    List<User> searchUsersByPartialUsername(String partialUsername);
    User updateUser(User user);
}
