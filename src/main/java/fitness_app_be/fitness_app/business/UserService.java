package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.CustomFileUploadException;
import fitness_app_be.fitness_app.exception_handling.UserProfileUpdateException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long id);
    User createUser(User user);
    void deleteUser(long userId);
    Optional<User> findUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    List<User> searchUsersByPartialUsername(String partialUsername);
    User updateUser(User user);
    User uploadUserProfilePicture(Long userId, MultipartFile imageFile) throws CustomFileUploadException, UserProfileUpdateException;
    User attachedDietToUser(Long UserId, Diet diet);
    List<Long> getUsersWithWorkout(Long workoutId);
}
