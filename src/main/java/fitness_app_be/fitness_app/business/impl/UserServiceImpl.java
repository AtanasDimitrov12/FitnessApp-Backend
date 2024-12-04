package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.CustomFileUploadException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserProfileUpdateException;
import fitness_app_be.fitness_app.exception_handling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;


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
        return userRepository.findByUsername(username);
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

            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return userRepository.update(user);
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public User uploadUserProfilePicture(Long userId, MultipartFile imageFile) throws CustomFileUploadException, UserProfileUpdateException {
        try {
            // Upload the image to the cloud directly using MultipartFile
            String imageUrl = uploadImageToCloud(imageFile);

            // Update the user's profile with the image URL
            User user = getUserById(userId);
            user.setPictureURL(imageUrl);

            // Save the updated user
            return userRepository.update(user);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CustomFileUploadException("Image upload failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserProfileUpdateException("Error updating user profile: " + e.getMessage(), e);
        }
    }

    private String uploadImageToCloud(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty.");
        }

        // Get the current system time
        Date currentDate = new Date();
        System.out.println("Current system time: " + currentDate);

        // Get the current timestamp in seconds
        long timestamp = System.currentTimeMillis() / 1000L;
        System.out.println("Timestamp sent to Cloudinary: " + timestamp);

        // Convert timestamp to readable date for comparison
        Date timestampDate = new Date(timestamp * 1000L);
        System.out.println("Timestamp date: " + timestampDate);

        // Upload parameters
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "timestamp", timestamp
        );

        // Upload the image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(
                multipartFile.getBytes(),
                uploadParams
        );

        // Retrieve the URL
        Object url = uploadResult.get("url");
        if (url instanceof String) {
            return (String) url;
        } else {
            throw new IOException("Failed to retrieve image URL from upload result.");
        }
    }




}




