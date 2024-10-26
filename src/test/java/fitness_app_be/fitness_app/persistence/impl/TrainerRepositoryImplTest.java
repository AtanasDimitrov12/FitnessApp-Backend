package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaTrainerRepository;
import fitness_app_be.fitness_app.persistence.mapper.TrainerEntityMapper;
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

class TrainerRepositoryImplTest {

    @Mock
    private JpaTrainerRepository jpaTrainerRepository;

    @Mock
    private TrainerEntityMapper trainerEntityMapper;

    @InjectMocks
    private TrainerRepositoryImpl trainerRepositoryImpl;

    private Trainer mockTrainer;
    private TrainerEntity mockTrainerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockTrainer = new Trainer(1L, "John", "Doe", "johndoe", "john.doe@example.com", 35, "Male", "Increase Muscle Mass", "./images/trainer.jpg", null, null);
        mockTrainerEntity = new TrainerEntity(1L, "John", "Doe", "johndoe", "john.doe@example.com", 35, "Male", "Increase Muscle Mass", "./images/trainer.jpg", null, null);
    }

    @Test
    void exists() {
        when(jpaTrainerRepository.existsById(1L)).thenReturn(true);

        boolean exists = trainerRepositoryImpl.exists(1L);

        assertTrue(exists, "Trainer should exist with ID 1.");
        verify(jpaTrainerRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaTrainerRepository.findAll()).thenReturn(Arrays.asList(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        List<Trainer> trainers = trainerRepositoryImpl.getAll();

        assertNotNull(trainers, "The list of trainers should not be null.");
        assertEquals(1, trainers.size(), "The number of trainers returned does not match.");
        assertEquals("johndoe", trainers.get(0).getUsername(), "The trainer username does not match.");

        verify(jpaTrainerRepository, times(1)).findAll();
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void create() {
        when(trainerEntityMapper.toEntity(mockTrainer)).thenReturn(mockTrainerEntity);
        when(jpaTrainerRepository.save(mockTrainerEntity)).thenReturn(mockTrainerEntity);
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Trainer createdTrainer = trainerRepositoryImpl.create(mockTrainer);

        assertNotNull(createdTrainer, "The created trainer should not be null.");
        assertEquals("johndoe", createdTrainer.getUsername(), "The trainer username does not match.");

        verify(trainerEntityMapper, times(1)).toEntity(mockTrainer);
        verify(jpaTrainerRepository, times(1)).save(mockTrainerEntity);
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void update() {
        when(trainerEntityMapper.toEntity(mockTrainer)).thenReturn(mockTrainerEntity);
        when(jpaTrainerRepository.save(mockTrainerEntity)).thenReturn(mockTrainerEntity);
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Trainer updatedTrainer = trainerRepositoryImpl.update(mockTrainer);

        assertNotNull(updatedTrainer, "The updated trainer should not be null.");
        assertEquals("johndoe", updatedTrainer.getUsername(), "The trainer username does not match.");

        verify(trainerEntityMapper, times(1)).toEntity(mockTrainer);
        verify(jpaTrainerRepository, times(1)).save(mockTrainerEntity);
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void delete() {
        trainerRepositoryImpl.delete(1L);

        verify(jpaTrainerRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTrainerById() {
        when(jpaTrainerRepository.findById(1L)).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Optional<Trainer> trainer = trainerRepositoryImpl.getTrainerById(1L);

        assertTrue(trainer.isPresent(), "The trainer should be present.");
        assertEquals("johndoe", trainer.get().getUsername(), "The trainer username does not match.");

        verify(jpaTrainerRepository, times(1)).findById(1L);
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void findByEmail() {
        when(jpaTrainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Optional<Trainer> trainer = trainerRepositoryImpl.findByEmail("john.doe@example.com");

        assertTrue(trainer.isPresent(), "The trainer should be present.");
        assertEquals("john.doe@example.com", trainer.get().getEmail(), "The trainer email does not match.");

        verify(jpaTrainerRepository, times(1)).findByEmail("john.doe@example.com");
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void findByUsername() {
        when(jpaTrainerRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Optional<Trainer> trainer = trainerRepositoryImpl.findByUsername("johndoe");

        assertTrue(trainer.isPresent(), "The trainer should be present.");
        assertEquals("johndoe", trainer.get().getUsername(), "The trainer username does not match.");

        verify(jpaTrainerRepository, times(1)).findByUsername("johndoe");
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void findByExpertise() {
        when(jpaTrainerRepository.findByExpertiseContainingIgnoreCase("Muscle")).thenReturn(Arrays.asList(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        List<Trainer> trainers = trainerRepositoryImpl.findByExpertise("Muscle");

        assertNotNull(trainers, "The list of trainers should not be null.");
        assertEquals(1, trainers.size(), "The number of trainers returned does not match.");
        assertEquals("Increase Muscle Mass", trainers.get(0).getExpertise(), "The trainer expertise does not match.");

        verify(jpaTrainerRepository, times(1)).findByExpertiseContainingIgnoreCase("Muscle");
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void findByUsernameContainingIgnoreCase() {
        when(jpaTrainerRepository.findByUsernameContainingIgnoreCase("john")).thenReturn(Arrays.asList(mockTrainerEntity));
        when(trainerEntityMapper.toDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        List<Trainer> trainers = trainerRepositoryImpl.findByUsernameContainingIgnoreCase("john");

        assertNotNull(trainers, "The list of trainers should not be null.");
        assertEquals(1, trainers.size(), "The number of trainers returned does not match.");
        assertEquals("johndoe", trainers.get(0).getUsername(), "The trainer username does not match.");

        verify(jpaTrainerRepository, times(1)).findByUsernameContainingIgnoreCase("john");
        verify(trainerEntityMapper, times(1)).toDomain(mockTrainerEntity);
    }

    @Test
    void countByEmail() {
        when(jpaTrainerRepository.countByEmail("john.doe@example.com")).thenReturn(1L);

        long count = trainerRepositoryImpl.countByEmail("john.doe@example.com");

        assertEquals(1L, count, "The count of trainers with the email does not match.");
        verify(jpaTrainerRepository, times(1)).countByEmail("john.doe@example.com");
    }
}
