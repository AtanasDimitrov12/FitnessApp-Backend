package fitness_app_be.fitness_app.BusinessLayer;

import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.UserMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.User;
import fitness_app_be.fitness_app.PersistenceLayer.UserRepository;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock objects
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");

        mockUserDTO = new UserDTO();
        mockUserDTO.setId(1L);
        mockUserDTO.setUsername("testUser");
        mockUserDTO.setEmail("test@example.com");
    }

    @Test
    void getAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(userRepository.findAll()).thenReturn(users); // Return a list of mock users
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        // Act
        List<UserDTO> userDTOList = userService.getAllUsers();

        // Assert
        assertNotNull(userDTOList);
        assertEquals(1, userDTOList.size());
        assertEquals("testUser", userDTOList.get(0).getUsername());

        // Verify interactions
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(mockUser);
    }



    @Test
    void getUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        // Act
        UserDTO userDTO = userService.getUserById(1L);

        // Assert
        assertNotNull(userDTO);
        assertEquals("testUser", userDTO.getUsername());

        // Verify interactions
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void getUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));

        // Verify interactions
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void createUser() {
        // Arrange
        when(userMapper.toEntity(mockUserDTO)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        // Act
        UserDTO savedUserDTO = userService.createUser(mockUserDTO);

        // Assert
        assertNotNull(savedUserDTO);
        assertEquals("testUser", savedUserDTO.getUsername());

        // Verify interactions
        verify(userMapper, times(1)).toEntity(mockUserDTO);
        verify(userRepository, times(1)).save(mockUser);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void deleteUser() {
        // Act
        userService.deleteUser(1L);

        // Verify that deleteById was called on the repository
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findUserByEmail() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        // Act
        UserDTO foundUserDTO = userService.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(foundUserDTO);
        assertEquals("test@example.com", foundUserDTO.getEmail());

        // Verify interactions
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toDto(mockUser);
    }

}
