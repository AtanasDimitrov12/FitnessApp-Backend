package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        mockUser = new User(1L, "testUser", "test@example.com", "Gain muscle", "Vegetarian", "./images/user.jpg");
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(mockUser);
        when(userRepository.getAll()).thenReturn(users);

        List<User> userList = userServiceImpl.getAllUsers();

        assertNotNull(userList, "The returned list of users should not be null.");
        assertEquals(1, userList.size(), "The size of the user list does not match.");
        assertEquals("testUser", userList.get(0).getUsername(), "The username does not match.");

        verify(userRepository, times(1)).getAll();
    }

    @Test
    void getUserById() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(mockUser));

        User user = userServiceImpl.getUserById(1L);

        assertNotNull(user, "The returned user should not be null.");
        assertEquals("testUser", user.getUsername(), "The username does not match.");

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
        assertEquals("testUser", createdUser.getUsername());

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
        assertEquals("test@example.com", foundUser.get().getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void searchUsersByPartialUsername() {
        List<User> users = Arrays.asList(mockUser);
        String partialUsername = "test";

        when(userRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(users);

        List<User> result = userServiceImpl.searchUsersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "The number of users returned does not match.");
        assertEquals("testUser", result.get(0).getUsername(), "The username does not match.");

        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Test
    void updateUser() {
        when(userRepository.exists(mockUser.getId())).thenReturn(true);
        when(userRepository.update(mockUser)).thenReturn(mockUser);

        User updatedUser = userServiceImpl.updateUser(mockUser);

        assertNotNull(updatedUser, "The updated user should not be null.");
        assertEquals("testUser", updatedUser.getUsername(), "The username does not match.");

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
