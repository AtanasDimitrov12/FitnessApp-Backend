package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample workouts, diets, and notes for testing
        List<Workout> workouts = new ArrayList<>(); // Add mock Workout objects if necessary
        List<Diet> diets = new ArrayList<>(); // Add mock Diet objects if necessary
        List<ProgressNote> notes = new ArrayList<>(); // Add mock ProgressNote objects if necessary

        // Initialize mockUser with the workouts, diets, and notes
        mockUser = new User(
                1L,
                "testUser",
                "test@example.com",
                "Gain muscle",
                "Vegetarian",
                "./images/user.jpg",
                workouts,
                diets,
                notes
        );
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(mockUser);
        when(userRepository.getAll()).thenReturn(users);

        List<User> userList = userServiceImpl.getAllUsers();

        assertNotNull(userList, "The returned list of users should not be null.");
        assertEquals(1, userList.size(), "The size of the user list does not match.");
        assertEquals(mockUser, userList.get(0), "The user does not match expected value.");

        verify(userRepository, times(1)).getAll();
    }

    @Test
    void getUserById() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(mockUser));

        User user = userServiceImpl.getUserById(1L);

        assertNotNull(user, "The returned user should not be null.");
        assertEquals(mockUser, user, "The user does not match expected value.");

        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_UserNotFound() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(1L));

        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void createUser() {
        when(userRepository.create(mockUser)).thenReturn(mockUser);

        User createdUser = userServiceImpl.createUser(mockUser);

        assertNotNull(createdUser, "The created user should not be null.");
        assertEquals(mockUser, createdUser, "The created user does not match expected value.");

        verify(userRepository, times(1)).create(mockUser);
    }

    @Test
    void deleteUser() {
        when(userRepository.exists(1L)).thenReturn(true);

        userServiceImpl.deleteUser(1L);

        verify(userRepository, times(1)).delete(1L);
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userRepository.exists(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUser(1L));

        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, never()).delete(1L);
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userServiceImpl.getUserByEmail("test@example.com");

        assertTrue(foundUser.isPresent(), "The returned user should not be null.");
        assertEquals(mockUser, foundUser.get(), "The found user does not match expected value.");

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void searchUsersByPartialUsername() {
        List<User> users = Arrays.asList(mockUser);
        String partialUsername = "test";

        when(userRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(users);

        List<User> result = userServiceImpl.searchUsersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "The number of users returned does not match.");
        assertEquals(mockUser, result.get(0), "The user does not match expected value.");

        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Test
    void updateUser() {
        when(userRepository.exists(mockUser.getId())).thenReturn(true);
        when(userRepository.update(mockUser)).thenReturn(mockUser);

        User updatedUser = userServiceImpl.updateUser(mockUser);

        assertNotNull(updatedUser, "The updated user should not be null.");
        assertEquals(mockUser, updatedUser, "The updated user does not match expected value.");

        verify(userRepository, times(1)).exists(mockUser.getId());
        verify(userRepository, times(1)).update(mockUser);
    }

    @Test
    void updateUser_UserNotFound() {
        when(userRepository.exists(mockUser.getId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUser(mockUser));

        verify(userRepository, times(1)).exists(mockUser.getId());
        verify(userRepository, never()).update(mockUser);
    }
}
