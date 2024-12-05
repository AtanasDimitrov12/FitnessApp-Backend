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
    private UserEntityMapper mapper;

    @InjectMocks
    private UserDietPreferenceRepositoryImpl userDietPreferenceRepository;

    private UserDietPreference preference;
    private UserDietPreferenceEntity preferenceEntity;
    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        preference = new UserDietPreference(1L, 101L, 2000, 3);
        userEntity = new UserEntity(1L, "testUser", "test@example.com", "password", null, null, "pictureURL", null, null, null, LocalDateTime.now(), LocalDateTime.now(), true, Role.USER);
        preferenceEntity = new UserDietPreferenceEntity(1L, userEntity, 2000, 3);
        user = new User(101L, "testUser", "test@example.com", "password", null, null, "pictureURL", LocalDateTime.now(), LocalDateTime.now(), Role.USER, null, null, null, true);
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
        // Mock the user repository to return a valid UserEntity
        when(userRepository.findEntityById(preference.getUserid())).thenReturn(userEntity);

        // Mock the mapper to convert the domain object to the entity
        when(userDietPreferenceEntityMapper.toEntity(preference, userEntity)).thenReturn(preferenceEntity);

        // Mock the repository save operation
        when(jpaUserDietPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);

        // Mock the mapper to convert the saved entity back to the domain object
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        // Mock the mapper for user update
        when(mapper.toDomain(userEntity)).thenReturn(user);

        // Call the create method
        UserDietPreference createdPreference = userDietPreferenceRepository.create(preference);

        // Verify the results
        assertNotNull(createdPreference);
        assertEquals(preference, createdPreference);

        // Verify method interactions
        verify(userRepository, times(1)).findEntityById(preference.getUserid());
        verify(jpaUserDietPreferenceRepository, times(1)).save(preferenceEntity);
        verify(userRepository, times(1)).update(user);
    }


    @Test
    void update_ShouldReturnUpdatedPreference_WhenPreferenceExists() {
        // Mock an existing preference entity
        when(jpaUserDietPreferenceRepository.findByUserId(preference.getUserid()))
                .thenReturn(Optional.of(preferenceEntity));

        // Mock the save operation
        when(jpaUserDietPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);

        // Mock the mapping back to domain
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        // Call the update method
        UserDietPreference updatedPreference = userDietPreferenceRepository.update(preference);

        // Verify and assert the results
        assertNotNull(updatedPreference);
        assertEquals(preference, updatedPreference);

        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(preference.getUserid());
        verify(jpaUserDietPreferenceRepository, times(1)).save(preferenceEntity);
    }

    @Test
    void update_ShouldThrowException_WhenPreferenceDoesNotExist() {
        // Mock the repository to return an empty Optional
        when(jpaUserDietPreferenceRepository.findByUserId(preference.getUserid()))
                .thenReturn(Optional.empty());

        // Assert that the exception is thrown
        assertThrows(IllegalArgumentException.class, () -> userDietPreferenceRepository.update(preference));

        // Verify that the method interactions occurred as expected
        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(preference.getUserid());
        verify(jpaUserDietPreferenceRepository, never()).save(any());
    }


    @Test
    void delete_ShouldDeletePreference_WhenExists() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(true);
        userDietPreferenceRepository.delete(1L);
        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
        verify(jpaUserDietPreferenceRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenPreferenceDoesNotExist() {
        when(jpaUserDietPreferenceRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userDietPreferenceRepository.delete(1L));
        verify(jpaUserDietPreferenceRepository, times(1)).existsById(1L);
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
        when(jpaUserDietPreferenceRepository.findByUserId(101L)).thenReturn(Optional.of(preferenceEntity));
        when(userDietPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.findByUserId(101L);

        assertTrue(foundPreference.isPresent());
        assertEquals(preference, foundPreference.get());
        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(101L);
    }

    @Test
    void findByUserId_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserDietPreferenceRepository.findByUserId(101L)).thenReturn(Optional.empty());
        Optional<UserDietPreference> foundPreference = userDietPreferenceRepository.findByUserId(101L);
        assertTrue(foundPreference.isEmpty());
        verify(jpaUserDietPreferenceRepository, times(1)).findByUserId(101L);
    }
}
