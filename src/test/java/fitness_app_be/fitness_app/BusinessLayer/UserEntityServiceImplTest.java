package fitness_app_be.fitness_app.BusinessLayer;

import fitness_app_be.fitness_app.BusinessLayer.Impl.UserServiceImpl;
import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.UserMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
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

    private UserEntity mockUserEntity;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L);
        mockUserEntity.setUsername("testUser");
        mockUserEntity.setEmail("test@example.com");

        mockUser = new User(1L, "testUser", "test@example.com", "Gain muscle", "Vegetarian");
    }

    @Test
    void getAllUsers() {
        List<UserEntity> userEntities = Arrays.asList(mockUserEntity);
        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.entityToDomain(mockUserEntity)).thenReturn(mockUser);

        List<User> userList = userServiceImpl.getAllUsers();

        assertNotNull(userList, "Null list of users is returned.");
        assertEquals(1, userList.size(), "Returned size of users list does not match expected one.");
        assertEquals("testUser", userList.get(0).getUsername(), "Returned username does not match expected one.");

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).entityToDomain(mockUserEntity);
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.entityToDomain(mockUserEntity)).thenReturn(mockUser);

        User user = userServiceImpl.getUserById(1L);

        assertNotNull(user, "Null user is returned.");
        assertEquals("testUser", user.getUsername(), "Incorrect username.");

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).entityToDomain(mockUserEntity);
    }

    @Test
    void getUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).entityToDomain(any());
    }

    @Test
    void createUser() {
        when(userMapper.domainToEntity(mockUser)).thenReturn(mockUserEntity);
        when(userRepository.save(mockUserEntity)).thenReturn(mockUserEntity);
        when(userMapper.entityToDomain(mockUserEntity)).thenReturn(mockUser);

        User createdUser = userServiceImpl.createUser(mockUser);

        assertNotNull(createdUser, "Null user is returned.");
        assertEquals("testUser", createdUser.getUsername());

        verify(userMapper, times(1)).domainToEntity(mockUser);
        verify(userRepository, times(1)).save(mockUserEntity);
        verify(userMapper, times(1)).entityToDomain(mockUserEntity);
    }

    @Test
    void deleteUser() {
        userServiceImpl.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.entityToDomain(mockUserEntity)).thenReturn(mockUser);

        User foundUser = userServiceImpl.getUserByEmail("test@example.com");

        assertNotNull(foundUser, "Null user is returned.");
        assertEquals("test@example.com", foundUser.getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).entityToDomain(mockUserEntity);
    }

    @Test
    void searchUsersByPartialUsername() {
        String partialUsername = "test";
        List<UserEntity> userEntities = Arrays.asList(mockUserEntity);
        List<User> userList = Arrays.asList(mockUser);

        when(userRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(userEntities);
        when(userMapper.entityToDomain(mockUserEntity)).thenReturn(mockUser);

        List<User> result = userServiceImpl.searchUsersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "Incorrect number of users returned.");
        assertEquals("testUser", result.get(0).getUsername(), "Username does not match expected one.");
    }
}
