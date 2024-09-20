package fitness_app_be.fitness_app.BusinessLayer;

import fitness_app_be.fitness_app.BusinessLayer.Impl.UserServiceImpl;
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

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        List<User> users = Arrays.asList(mockUser);
        when(userRepository.findAll()).thenReturn(users); // Return a list of mock users
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        List<UserDTO> userDTOList = userServiceImpl.getAllUsers();

        assertNotNull(userDTOList, "Null list of users is returned.");
        assertEquals(1, userDTOList.size(), "Returned size users list does not match expected one.");
        assertEquals("testUser", userDTOList.get(0).getUsername(), "Returned first username does not match expected one.");

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(mockUser);

    }



    @Test
    void getUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UserDTO userDTO = userServiceImpl.getUserById(1L);

        assertNotNull(userDTO, "Null user is returned.");
        assertEquals("testUser", userDTO.getUsername(), "Incorrect username.");

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void getUserById_UserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void createUser() {

        when(userMapper.toEntity(mockUserDTO)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UserDTO savedUserDTO = userServiceImpl.createUser(mockUserDTO);

        assertNotNull(savedUserDTO, "Null user is returned.");
        assertEquals("testUser", savedUserDTO.getUsername());

        verify(userMapper, times(1)).toEntity(mockUserDTO);
        verify(userRepository, times(1)).save(mockUser);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void deleteUser() {

        userServiceImpl.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findUserByEmail() {

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UserDTO foundUserDTO = userServiceImpl.getUserByEmail("test@example.com");

        assertNotNull(foundUserDTO, "Null user is returned.");
        assertEquals("test@example.com", foundUserDTO.getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void searchUsersByPartialUsername() {

        String partialUsername = "test";
        List<User> users = Arrays.asList(mockUser);
        List<UserDTO> userDTOList = Arrays.asList(mockUserDTO);

        when(userRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(users);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        List<UserDTO> result = userServiceImpl.searchUsersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "Incorrect number of users returned.");
        assertEquals("testUser", result.get(0).getUsername(), "Username does not match expected one.");
    }

}
