package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private UserEntityMapper userMapper;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        List<ProgressNote> notes = new ArrayList<>();

        user = new User(1L, "testUser", "test@example.com", "password", null, null, "pictureURL", LocalDateTime.now(), LocalDateTime.now(), Role.USER, null, null, notes, true);
        List<ProgressNoteEntity> notesEntity = new ArrayList<>();
        userEntity = new UserEntity(1L, "testUser", "test@example.com", "password", null, null, "pictureURL", null, null, null, null, LocalDateTime.now(), LocalDateTime.now(), true, Role.USER);

    }

    @Test
    void exists_ShouldReturnTrue_WhenUserExists() {
        when(jpaUserRepository.existsById(1L)).thenReturn(true);

        assertTrue(userRepository.exists(1L));
        verify(jpaUserRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(jpaUserRepository.existsById(1L)).thenReturn(false);

        assertFalse(userRepository.exists(1L));
        verify(jpaUserRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfUsers() {
        when(jpaUserRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        List<User> users = userRepository.getAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(jpaUserRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void create_ShouldReturnCreatedUser() {
        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(jpaUserRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User createdUser = userRepository.create(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
        verify(jpaUserRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toEntity(user);
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void update_ShouldReturnUpdatedUser() {
        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(jpaUserRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User updatedUser = userRepository.update(user);

        assertNotNull(updatedUser);
        assertEquals(user, updatedUser);
        verify(jpaUserRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toEntity(user);
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void delete_ShouldDeleteUserById() {
        doNothing().when(jpaUserRepository).deleteById(1L);

        userRepository.delete(1L);

        verify(jpaUserRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> foundUser = userRepository.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(jpaUserRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.getUserById(1L);

        assertTrue(foundUser.isEmpty());
        verify(jpaUserRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDomain(any());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(jpaUserRepository, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void findByUsernameContainingIgnoreCase_ShouldReturnListOfUsers() {
        when(jpaUserRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(List.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        List<User> users = userRepository.findByUsernameContainingIgnoreCase("test");

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(jpaUserRepository, times(1)).findByUsernameContainingIgnoreCase("test");
        verify(userMapper, times(1)).toDomain(userEntity);
    }

    @Test
    void countByEmail_ShouldReturnCountOfUsersWithGivenEmail() {
        when(jpaUserRepository.countByEmail("test@example.com")).thenReturn(1L);

        long count = userRepository.countByEmail("test@example.com");

        assertEquals(1L, count);
        verify(jpaUserRepository, times(1)).countByEmail("test@example.com");
    }
}
