package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.CustomFileUploadException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserProfileUpdateException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Diet diet;
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        user = new User(1L, "JohnDoe", "john@example.com", "password123", null, null, null, null, null, Role.USER, null, null, null, true);
        diet = new Diet(1L, 1L, null);

        // Properly mocking Cloudinary uploader
        uploader = mock(Uploader.class);
        lenient().when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userRepository.getAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository, times(1)).getAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());  // Adjusted error message
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.create(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
        verify(userRepository, times(1)).create(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.exists(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(1L);
    }

    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.exists(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void findUserByUsername_ShouldReturnUser_WhenExists() {
        when(userRepository.findByUsername("JohnDoe")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserByUsername("JohnDoe");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("john@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser_WhenExists() {
        // Arrange
        when(userRepository.exists(user.getId())).thenReturn(true);
        when(userRepository.update(any(User.class))).thenReturn(user); // Mock update return

        // Act
        User updatedUser = userService.updateUser(user);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(user, updatedUser); // Ensure the returned user is the same as expected
        verify(userRepository, times(1)).exists(user.getId()); // Ensure existence check happens
        verify(userRepository, times(1)).update(user); // Ensure update is called once
    }


    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.exists(user.getId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void uploadUserProfilePicture_ShouldUploadAndReturnUpdatedUser() throws Exception {
        // Arrange
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(Map.of("url", "http://example.com/image.jpg"));
        when(userRepository.update(any(User.class))).thenReturn(user);

        // Act
        User updatedUser = userService.uploadUserProfilePicture(1L, multipartFile);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("http://example.com/image.jpg", updatedUser.getPictureURL());
    }

    @Test
    void uploadUserProfilePicture_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.uploadUserProfilePicture(1L, multipartFile));
    }

    @Test
    void uploadUserProfilePicture_ShouldThrowUserProfileUpdateException_WhenFileIsEmpty() {
        // Arrange
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        // Act & Assert
        assertThrows(UserProfileUpdateException.class, () -> userService.uploadUserProfilePicture(1L, emptyFile));

        // Verify necessary interactions only
        verify(emptyFile).isEmpty();
    }



    @Test
    void uploadUserProfilePicture_ShouldThrowCustomFileUploadException_WhenUploadFails() throws IOException {
        // Arrange
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Upload failed"));

        // Act & Assert
        assertThrows(CustomFileUploadException.class, () -> userService.uploadUserProfilePicture(1L, multipartFile));
    }


    @Test
    void attachedDietToUser_ShouldAttachDietAndReturnUser() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.update(any(User.class))).thenReturn(user);

        User updatedUser = userService.attachedDietToUser(1L, diet);

        assertNotNull(updatedUser);
        assertEquals(diet, updatedUser.getDiet());
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void getUsersWithWorkout_ShouldReturnUserIds() {
        List<Long> userIds = List.of(1L, 2L, 3L);
        when(userRepository.getUsersWithWorkout(1L)).thenReturn(userIds);

        List<Long> result = userService.getUsersWithWorkout(1L);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).getUsersWithWorkout(1L);
    }
}
