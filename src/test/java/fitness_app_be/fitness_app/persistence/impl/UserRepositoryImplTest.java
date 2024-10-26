package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
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

class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private UserEntityMapper userMapper;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    private User mockUser;
    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User(1L, "testUser", "test@example.com", "Gain muscle", "Vegetarian", "./images/user.jpg", null, null, null);
        mockUserEntity = new UserEntity(1L, "testUser", "test@example.com", "Gain muscle", "Vegetarian", "./images/user.jpg", null, null, null);
    }

    @Test
    void exists() {
        when(jpaUserRepository.existsById(1L)).thenReturn(true);

        boolean exists = userRepositoryImpl.exists(1L);

        assertTrue(exists, "User should exist with ID 1.");
        verify(jpaUserRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaUserRepository.findAll()).thenReturn(Arrays.asList(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        List<User> users = userRepositoryImpl.getAll();

        assertNotNull(users, "The list of users should not be null.");
        assertEquals(1, users.size(), "The number of users returned does not match.");
        assertEquals("testUser", users.get(0).getUsername(), "The username does not match.");

        verify(jpaUserRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void create() {
        when(userMapper.toEntity(mockUser)).thenReturn(mockUserEntity);
        when(jpaUserRepository.save(mockUserEntity)).thenReturn(mockUserEntity);
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        User createdUser = userRepositoryImpl.create(mockUser);

        assertNotNull(createdUser, "The created user should not be null.");
        assertEquals("testUser", createdUser.getUsername(), "The username does not match.");

        verify(userMapper, times(1)).toEntity(mockUser);
        verify(jpaUserRepository, times(1)).save(mockUserEntity);
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void update() {
        when(userMapper.toEntity(mockUser)).thenReturn(mockUserEntity);
        when(jpaUserRepository.save(mockUserEntity)).thenReturn(mockUserEntity);
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        User updatedUser = userRepositoryImpl.update(mockUser);

        assertNotNull(updatedUser, "The updated user should not be null.");
        assertEquals("testUser", updatedUser.getUsername(), "The username does not match.");

        verify(userMapper, times(1)).toEntity(mockUser);
        verify(jpaUserRepository, times(1)).save(mockUserEntity);
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void delete() {
        userRepositoryImpl.delete(1L);

        verify(jpaUserRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserById() {
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        Optional<User> user = userRepositoryImpl.getUserById(1L);

        assertTrue(user.isPresent(), "The user should be present.");
        assertEquals("testUser", user.get().getUsername(), "The username does not match.");

        verify(jpaUserRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void findByEmail() {
        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        Optional<User> user = userRepositoryImpl.findByEmail("test@example.com");

        assertTrue(user.isPresent(), "The user should be present.");
        assertEquals("test@example.com", user.get().getEmail(), "The email does not match.");

        verify(jpaUserRepository, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void findByUsername() {
        when(jpaUserRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        Optional<User> user = userRepositoryImpl.findByUsername("testUser");

        assertTrue(user.isPresent(), "The user should be present.");
        assertEquals("testUser", user.get().getUsername(), "The username does not match.");

        verify(jpaUserRepository, times(1)).findByUsername("testUser");
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void findByFitnessGoal() {
        when(jpaUserRepository.findByFitnessGoal("Gain muscle")).thenReturn(Arrays.asList(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        List<User> users = userRepositoryImpl.findByFitnessGoal("Gain muscle");

        assertNotNull(users, "The list of users should not be null.");
        assertEquals(1, users.size(), "The number of users returned does not match.");
        assertEquals("Gain muscle", users.get(0).getFitnessGoal(), "The fitness goal does not match.");

        verify(jpaUserRepository, times(1)).findByFitnessGoal("Gain muscle");
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void findByUsernameContainingIgnoreCase() {
        when(jpaUserRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(Arrays.asList(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);

        List<User> users = userRepositoryImpl.findByUsernameContainingIgnoreCase("test");

        assertNotNull(users, "The list of users should not be null.");
        assertEquals(1, users.size(), "The number of users returned does not match.");
        assertEquals("testUser", users.get(0).getUsername(), "The username does not match.");

        verify(jpaUserRepository, times(1)).findByUsernameContainingIgnoreCase("test");
        verify(userMapper, times(1)).toDomain(mockUserEntity);
    }

    @Test
    void countByEmail() {
        when(jpaUserRepository.countByEmail("test@example.com")).thenReturn(1L);

        long count = userRepositoryImpl.countByEmail("test@example.com");

        assertEquals(1L, count, "The count of users with the email does not match.");
        verify(jpaUserRepository, times(1)).countByEmail("test@example.com");
    }
}
