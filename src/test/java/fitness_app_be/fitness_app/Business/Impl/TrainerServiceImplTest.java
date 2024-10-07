package fitness_app_be.fitness_app.Business.Impl;

import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.ExceptionHandling.TrainerNotFoundException;
import fitness_app_be.fitness_app.Persistence.TrainerRepository;
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

class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockTrainer = new Trainer(1L, "John", "Doe", "johndoe", "john.doe@example.com", 35, "Male", "Increase Muscle Mass", "./images/trainer.jpg");
    }

    @Test
    void getAllTrainers() {
        List<Trainer> trainers = Arrays.asList(mockTrainer);
        when(trainerRepository.getAll()).thenReturn(trainers);

        List<Trainer> trainerList = trainerServiceImpl.getAllTrainers();

        assertNotNull(trainerList, "Trainer list should not be null.");
        assertEquals(1, trainerList.size(), "The size of the trainer list does not match.");
        assertEquals("johndoe", trainerList.get(0).getUsername(), "The trainer username does not match.");

        verify(trainerRepository, times(1)).getAll();
    }

    @Test
    void getTrainerById() {
        when(trainerRepository.getTrainerById(1L)).thenReturn(Optional.of(mockTrainer));

        Trainer trainer = trainerServiceImpl.getTrainerById(1L);

        assertNotNull(trainer, "Trainer should not be null.");
        assertEquals("johndoe", trainer.getUsername(), "Trainer username does not match.");

        verify(trainerRepository, times(1)).getTrainerById(1L);
    }

    @Test
    void getTrainerById_TrainerNotFound() {
        when(trainerRepository.getTrainerById(1L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerServiceImpl.getTrainerById(1L));

        verify(trainerRepository, times(1)).getTrainerById(1L);
    }

    @Test
    void createTrainer() {
        when(trainerRepository.create(mockTrainer)).thenReturn(mockTrainer);

        Trainer createdTrainer = trainerServiceImpl.createTrainer(mockTrainer);

        assertNotNull(createdTrainer, "Created trainer should not be null.");
        assertEquals("johndoe", createdTrainer.getUsername());

        verify(trainerRepository, times(1)).create(mockTrainer);
    }

    @Test
    void deleteTrainer() {
        when(trainerRepository.exists(1L)).thenReturn(true);

        trainerServiceImpl.deleteTrainer(1L);

        verify(trainerRepository, times(1)).delete(1L);
    }

    @Test
    void deleteTrainer_TrainerNotFound() {
        when(trainerRepository.exists(1L)).thenReturn(false);

        assertThrows(TrainerNotFoundException.class, () -> trainerServiceImpl.deleteTrainer(1L));

        verify(trainerRepository, times(1)).exists(1L);
        verify(trainerRepository, never()).delete(1L);
    }

    @Test
    void getTrainerByEmail() {
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockTrainer));

        Trainer foundTrainer = trainerServiceImpl.getTrainerByEmail("john.doe@example.com");

        assertNotNull(foundTrainer, "Found trainer should not be null.");
        assertEquals("john.doe@example.com", foundTrainer.getEmail());

        verify(trainerRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void searchTrainersByPartialUsername() {
        String partialUsername = "john";
        List<Trainer> trainers = Arrays.asList(mockTrainer);

        when(trainerRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(trainers);

        List<Trainer> result = trainerServiceImpl.searchTrainersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "Incorrect number of trainers returned.");
        assertEquals("johndoe", result.get(0).getUsername(), "Trainer username does not match expected value.");
    }

    @Test
    void updateTrainer() {
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockTrainer));
        when(trainerRepository.update(mockTrainer)).thenReturn(mockTrainer);

        mockTrainer.setExpertise("Increase Stamina");

        Trainer updatedTrainer = trainerServiceImpl.updateTrainer(mockTrainer);

        assertNotNull(updatedTrainer, "Updated trainer should not be null.");
        assertEquals("Increase Stamina", updatedTrainer.getExpertise(), "Trainer expertise did not update correctly.");

        verify(trainerRepository, times(1)).findByEmail("john.doe@example.com");
        verify(trainerRepository, times(1)).update(mockTrainer);
    }
}
