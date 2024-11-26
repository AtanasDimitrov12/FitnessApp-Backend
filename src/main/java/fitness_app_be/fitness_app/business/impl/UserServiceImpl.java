package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.exception_handling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User createUser(User user) {
        return userRepository.create(user);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.exists(userId)) {
            userRepository.delete(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username);
        } else {
            throw new UserNotFoundException(username + " not found");
        }

    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> searchUsersByPartialUsername(String partialUsername) {
        return userRepository.findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Override
    public User updateUser(User user) {
        if (userRepository.exists(user.getId())) {

            return userRepository.update(user);
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public User uploadUserProfilePicture(Long userId, File imageFile) throws IOException {


            try {
                // Upload the image to the cloud (e.g., Cloudinary, AWS S3, etc.)
                String imageUrl = uploadImageToCloud(imageFile);

                // Update the user's profile with the image URL
                User user = getUserById(userId);
                user.setPictureURL(imageUrl);

                // Save the updated user
                return userRepository.update(user);

            } catch (IllegalArgumentException e) {
                throw new WorkoutNotFoundException("Exercise validation failed: " + e.getMessage());
            } catch (IOException e) {
                throw new WorkoutNotFoundException("Image upload failed: " + e.getMessage());
            } catch (Exception e) {
                throw new WorkoutNotFoundException("Error creating workout: " + e.getMessage());
            }
        }

    private String uploadImageToCloud (File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File must not be null and must exist.");
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        Object url = uploadResult.get("url");
        if (url instanceof String urlString) {
            return urlString;
        } else {
            throw new IOException("Failed to retrieve image URL from upload result.");
        }
    }


}




