package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.UserDietPreferenceService;
import fitness_app_be.fitness_app.business.UserWorkoutPreferenceService;
import fitness_app_be.fitness_app.domain.*;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDietPreferenceService userDietPreferenceService;

    @Mock
    private UserWorkoutPreferenceService userWorkoutPreferenceService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        Diet diet = new Diet();
        List<Workout> workouts = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<String> goal = new ArrayList<>();
        List<String> style = new ArrayList<>();
        WorkoutPlan workoutPlan = new WorkoutPlan(1L, users, workouts, goal, style);
        List<ProgressNote> notes = new ArrayList<>();
        UserDietPreference userDietPreference = new UserDietPreference(1L, user, 2500, 3);
        UserWorkoutPreference userWorkoutPreference = new UserWorkoutPreference(1L, 1L, "Strength", "Beginner", "Evening", 4);
        user = new User(1L, "testUser", "test@example.com", "password", userDietPreference, userWorkoutPreference, "pictureURL", LocalDateTime.now(), LocalDateTime.now(),  Role.ADMIN,workoutPlan, diet, notes, true);

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

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void createUser_ShouldCreateUserSuccessfully() {
        // Arrange
        User user = new User(1L, "username", "email@example.com", "password", null, null, null, null, null, Role.USER, null, null, null, true);

        // Mocking repository
        when(userRepository.create(user)).thenReturn(user); // Mock user creation

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser, "Created user should not be null");
        assertEquals(user, createdUser, "Returned user should match the input");

        // Verify interactions
        verify(userRepository, times(1)).create(user);
    }




    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, times(1)).delete(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.exists(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, never()).delete(1L);
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void searchUsersByPartialUsername_ShouldReturnMatchingUsers() {
        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(List.of(user));

        List<User> users = userService.searchUsersByPartialUsername("test");

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase("test");
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        // Arrange
        user.setPassword("updatedPassword"); // Original plain password
        String encodedPassword = "encodedPassword";

        when(userRepository.exists(1L)).thenReturn(true); // Mock user existence
        when(passwordEncoder.encode("updatedPassword")).thenReturn(encodedPassword); // Mock password encoding

        // Mock userRepository.update() to verify and update the User password
        when(userRepository.update(any(User.class))).thenAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0); // Capture the User object passed
            updatedUser.setPassword(encodedPassword); // Simulate encoding the password
            return updatedUser; // Return the updated User
        });

        // Act
        User result = userService.updateUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword()); // Verify password is encoded
        verify(userRepository, times(1)).exists(1L);
        verify(passwordEncoder, times(1)).encode("updatedPassword"); // Ensure encoding is called
        verify(userRepository, times(1)).update(argThat(updatedUserRequest ->
                updatedUserRequest.getPassword().equals(encodedPassword) // Ensure encoded password is passed
        ));
    }




    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.exists(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, never()).update(user);
    }
}
