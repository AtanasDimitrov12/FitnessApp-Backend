package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.CustomFileUploadException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserProfileUpdateException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


            return userRepository.update(user);
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public User uploadUserProfilePicture(Long userId, MultipartFile imageFile)
            throws CustomFileUploadException, UserProfileUpdateException {
        // Validate image file before uploading
        if (imageFile == null || imageFile.isEmpty()) {
            throw new UserProfileUpdateException("File must not be null or empty.");
        }

        // Get user or throw exception if not found
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        try {
            // Upload the image to Cloudinary
            String imageUrl = uploadImageToCloud(imageFile);

            // Update user's profile picture
            user.setPictureURL(imageUrl);

            // Save updated user and return
            return userRepository.update(user);

        } catch (IOException e) {
            throw new CustomFileUploadException("Image upload failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserProfileUpdateException("Error updating user profile: " + e.getMessage(), e);
        }
    }

    private String uploadImageToCloud(MultipartFile multipartFile) throws IOException {
        // Validate file
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("File must not be null or empty.");
        }

        // Generate timestamp for Cloudinary upload
        long timestamp = System.currentTimeMillis() / 1000L;

        // Upload parameters
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "timestamp", timestamp
        );

        // Upload image to Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                multipartFile.getBytes(),
                uploadParams
        );

        // Ensure we get a valid URL from Cloudinary
        String imageUrl = (String) uploadResult.get("url");
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IOException("Failed to retrieve image URL from upload result.");
        }

        return imageUrl;
    }

    @Override
    public User attachedDietToUser(Long userId, Diet diet){
        User user = getUserById(userId);
        user.setDiet(diet);
        userRepository.update(user);
        return user;
    }

    @Override
    public List<Long> getUsersWithWorkout(Long workoutId) {
        return userRepository.getUsersWithWorkout(workoutId);
    }

}




