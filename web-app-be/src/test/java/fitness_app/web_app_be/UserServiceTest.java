package fitness_app.web_app_be;

import fitness_app.web_app_be.BusinessLayer.UserService;
import fitness_app.web_app_be.DTOsLayer.UserDTO;
import fitness_app.web_app_be.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app.web_app_be.MapperLayer.UserMapper;
import fitness_app.web_app_be.PersistenceLayer.Entity.User;
import fitness_app.web_app_be.PersistenceLayer.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class) // For Mockito to work
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User(userId, "john_doe", "john@example.com", "password", "Lose weight", "Vegetarian");
        UserDTO mockUserDTO = new UserDTO(userId, "john_doe", "john@example.com", "Lose weight", "Vegetarian");

        // Mock repository to return a user
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        // Mock mapper to convert user to UserDTO
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        // Act
        UserDTO result = userService.getUserById(userId);

        // Assert
        assertNotNull(result); // Result should not be null
        assertEquals(userId, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userRepository).findById(userId); // Verify findById was called
    }









    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        Long userId = 1L;

        // Mock repository to return empty Optional (user not found)
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));

        // Verify findById was called with the correct ID
        verify(userRepository).findById(userId);
    }





    @Test
    void testCreateUser_Success() {
        // Arrange
        UserDTO mockUserDTO = new UserDTO(null, "jane_doe", "jane@example.com", "Lose weight", "Keto");
        User mockUser = new User(null, "jane_doe", "jane@example.com", "password", "Lose weight", "Keto");
        User savedUser = new User(1L, "jane_doe", "jane@example.com", "password", "Lose weight", "Keto");
        UserDTO savedUserDTO = new UserDTO(1L, "jane_doe", "jane@example.com", "Lose weight", "Keto");

        // Mock mapper to convert UserDTO to User entity
        when(userMapper.toEntity(mockUserDTO)).thenReturn(mockUser);
        // Mock repository save operation
        when(userRepository.save(mockUser)).thenReturn(savedUser);
        // Mock mapper to convert saved User entity back to UserDTO
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDTO);

        // Act
        UserDTO result = userService.createUser(mockUserDTO);

        // Assert
        assertNotNull(result); // Result should not be null
        assertEquals(1L, result.getId());
        assertEquals("jane_doe", result.getUsername());
        verify(userRepository).save(mockUser); // Verify save was called on the repository
    }







}

