package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.UserWorkoutPreferenceEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserWorkoutPreferenceRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserWorkoutPreferenceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWorkoutPreferenceRepositoryImplTest {

    @Mock
    private JpaUserWorkoutPreferenceRepository jpaUserWorkoutPreferenceRepository;

    @Mock
    private UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper;

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private UserWorkoutPreferenceRepositoryImpl userWorkoutPreferenceRepository;

    private UserWorkoutPreference preference;
    private UserWorkoutPreferenceEntity preferenceEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        preference = new UserWorkoutPreference(1L, 101L, "Strength", "Beginner", "Evening", 4);
        userEntity = new UserEntity(101L, "testUser", "test@example.com", "password", null, null, "pictureURL", null, null, null);
        preferenceEntity = new UserWorkoutPreferenceEntity(1L, userEntity, "Strength", "Beginner", "Evening", 4);
    }

    @Test
    void exists_ShouldReturnTrue_WhenPreferenceExists() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(true);

        assertTrue(userWorkoutPreferenceRepository.exists(1L));
        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenPreferenceDoesNotExist() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(false);

        assertFalse(userWorkoutPreferenceRepository.exists(1L));
        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void create_ShouldReturnCreatedPreference() {
        when(userRepository.findById(101L)).thenReturn(Optional.of(userEntity));
        when(userWorkoutPreferenceEntityMapper.toEntity(preference, userEntity)).thenReturn(preferenceEntity);
        when(jpaUserWorkoutPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);
        when(userWorkoutPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        UserWorkoutPreference createdPreference = userWorkoutPreferenceRepository.create(preference);

        assertNotNull(createdPreference);
        assertEquals(preference, createdPreference);
        verify(jpaUserWorkoutPreferenceRepository, times(1)).save(preferenceEntity);
    }

    @Test
    void update_ShouldReturnUpdatedPreference_WhenPreferenceExists() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(101L)).thenReturn(Optional.of(userEntity));
        when(userWorkoutPreferenceEntityMapper.toEntity(preference, userEntity)).thenReturn(preferenceEntity);
        when(jpaUserWorkoutPreferenceRepository.save(preferenceEntity)).thenReturn(preferenceEntity);
        when(userWorkoutPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        UserWorkoutPreference updatedPreference = userWorkoutPreferenceRepository.update(preference);

        assertNotNull(updatedPreference);
        assertEquals(preference, updatedPreference);
        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
        verify(jpaUserWorkoutPreferenceRepository, times(1)).save(preferenceEntity);
    }

    @Test
    void update_ShouldThrowException_WhenPreferenceDoesNotExist() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userWorkoutPreferenceRepository.update(preference));
        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void delete_ShouldDeletePreference_WhenExists() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(true);

        userWorkoutPreferenceRepository.delete(1L);

        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
        verify(jpaUserWorkoutPreferenceRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenPreferenceDoesNotExist() {
        when(jpaUserWorkoutPreferenceRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userWorkoutPreferenceRepository.delete(1L));
        verify(jpaUserWorkoutPreferenceRepository, times(1)).existsById(1L);
    }

    @Test
    void getWorkoutPreferenceById_ShouldReturnPreference_WhenExists() {
        when(jpaUserWorkoutPreferenceRepository.findById(1L)).thenReturn(Optional.of(preferenceEntity));
        when(userWorkoutPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        Optional<UserWorkoutPreference> foundPreference = userWorkoutPreferenceRepository.getWorkoutPreferenceById(1L);

        assertTrue(foundPreference.isPresent());
        assertEquals(preference, foundPreference.get());
        verify(jpaUserWorkoutPreferenceRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutPreferenceById_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserWorkoutPreferenceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserWorkoutPreference> foundPreference = userWorkoutPreferenceRepository.getWorkoutPreferenceById(1L);

        assertTrue(foundPreference.isEmpty());
        verify(jpaUserWorkoutPreferenceRepository, times(1)).findById(1L);
    }

    @Test
    void findByUserId_ShouldReturnPreference_WhenExists() {
        when(jpaUserWorkoutPreferenceRepository.findByUserId(101L)).thenReturn(Optional.of(preferenceEntity));
        when(userWorkoutPreferenceEntityMapper.toDomain(preferenceEntity)).thenReturn(preference);

        Optional<UserWorkoutPreference> foundPreference = userWorkoutPreferenceRepository.findByUserId(101L);

        assertTrue(foundPreference.isPresent());
        assertEquals(preference, foundPreference.get());
        verify(jpaUserWorkoutPreferenceRepository, times(1)).findByUserId(101L);
    }

    @Test
    void findByUserId_ShouldReturnEmpty_WhenDoesNotExist() {
        when(jpaUserWorkoutPreferenceRepository.findByUserId(101L)).thenReturn(Optional.empty());

        Optional<UserWorkoutPreference> foundPreference = userWorkoutPreferenceRepository.findByUserId(101L);

        assertTrue(foundPreference.isEmpty());
        verify(jpaUserWorkoutPreferenceRepository, times(1)).findByUserId(101L);
    }
}
