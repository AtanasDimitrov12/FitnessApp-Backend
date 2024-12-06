package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserDietPreferenceRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserDietPreferenceEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDietPreferenceRepositoryImplTest {

    @Mock
    private JpaUserDietPreferenceRepository jpaUserDietPreferenceRepository;

    @Mock
    private UserDietPreferenceEntityMapper userDietPreferenceEntityMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserDietPreferenceRepositoryImpl userDietPreferenceRepository;

    private User user;
    private UserEntity userEntity;
    private UserDietPreference preference;
    private UserDietPreferenceEntity preferenceEntity;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userEntity = new UserEntity(
                1L, "testUser", "test@example.com", "password", null, null,
                "pictureURL", null, null, null,
                LocalDateTime.now(), LocalDateTime.now(), true, Role.USER
        );

        preference = new UserDietPreference(1L, user, 2000, 3);
        preferenceEntity = new UserDietPreferenceEntity(1L, userEntity, 2000, 3);
    }

    @Test
    void exists_ShouldReturnTrue_WhenPreferenceExists() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(true);

        assertTrue(userDietPreferenceRepository.exists(1L));

        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenPreferenceDoesNotExist() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(false);

        assertFalse(userDietPreferenceRepository.exists(1L));

        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void create_ShouldReturnCreatedPreference() {
        when(userRepository.findEntityById(user.getId())).thenReturn(userEntity);
        when(userDietPreferenceEntityMapper.toEntity(preference, userEntity)).thenReturn(preferenceEntity);
        when(jpaUserDietPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        UserDietPreference createdPreference = userDietPreferenceRepository.create(preference);

        assertNotNull(createdPreference);
        assertEquals(preference, createdPreference);

        verify(userRepository, times(1)).findEntityById(user.getId());
        verify(jpaUserDietPreferenceRepository, times(1)).save(preferenceEntity);
        verify(userDietPreferenceEntityMapper, times(1)).toDomain(preferenceEntity);
    }

    @Test
    void create_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findEntityById(user.getId())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDietPreferenceRepository.create(preference)
        );

        assertEquals("User with ID 1 does not exist.", exception.getMessage());

        verify(userRepository, times(1)).findEntityById(user.getId());
        verify(jpaUserDietPreferenceRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUpdatedPreference() {
        when(jpaUserDietPreferenceRepository.findByUserId(user.getId())).thenReturn(Optional.of(preferenceEntity));
        when(jpaUserDietPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        UserDietPreference updatedPreference = userDietPreferenceRepository.update(preference);

        assertNotNull(updatedPreference);
        assertEquals(preference, updatedPreference);

        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(user.getId());
        verify(jpaUserDietPreferenceRepository, times(1)).save(preferenceEntity);
    }

    @Test
    void update_ShouldThrowException_WhenPreferenceDoesNotExist() {
        when(jpaUserDietPreferenceRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDietPreferenceRepository.update(preference)
        );

        assertEquals("UserDietPreference for user ID 1 does not exist.", exception.getMessage());

        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(user.getId());
        verify(jpaUserDietPreferenceRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeletePreference() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userDietPreferenceRepository.delete(1L));

        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
        verify(jpaUserDietPreferenceRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenPreferenceDoesNotExist() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDietPreferenceRepository.delete(1L)
        );

        assertEquals("UserDietPreference with ID 1 does not exist.", exception.getMessage());

        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
        verify(jpaUserDietPreferenceRepository, never()).deleteById(anyLong());
    }

    @Test
    void getDietPreferenceById_ShouldReturnPreference_WhenExists() {
        when(jpaUserDietPreferenceRepository.findById(1L)).thenReturn(Optional.of(preferenceEntity));
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.getDietPreferenceById(1L);

        assertTrue(foundPreference.isPresent());
        assertEquals(preference, foundPreference.get());

        verify(jpaUserDietPreferenceRepository, times(1)).findById(1L);
    }

    @Test
    void getDietPreferenceById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserDietPreferenceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.getDietPreferenceById(1L);

        assertTrue(foundPreference.isEmpty());

        verify(jpaUserDietPreferenceRepository, times(1)).findById(1L);
    }

    @Test
    void findByUserId_ShouldReturnPreference_WhenExists() {
        when(jpaUserDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(preferenceEntity));
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.findByUserId(1L);

        assertTrue(foundPreference.isPresent());
        assertEquals(preference, foundPreference.get());

        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findByUserId_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserDietPreferenceRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.findByUserId(1L);

        assertTrue(foundPreference.isEmpty());

        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(1L);
    }
}
