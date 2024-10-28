package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        List<ProgressNote> notes = new ArrayList<>();
        user = new User(1L, "testUser", "test@example.com", "password", "muscle gain", "low carbs", "pictureURL", 1L, 1L, notes);

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
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.create(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
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
        when(userRepository.exists(1L)).thenReturn(true);
        when(userRepository.update(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user, updatedUser);
        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.exists(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
        verify(userRepository, times(1)).exists(1L);
        verify(userRepository, never()).update(user);
    }
}
