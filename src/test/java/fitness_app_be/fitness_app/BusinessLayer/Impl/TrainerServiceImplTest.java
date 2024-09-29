package fitness_app_be.fitness_app.BusinessLayer.Impl;

import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.TrainerNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.TrainerMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.TrainerEntity;
import fitness_app_be.fitness_app.PersistenceLayer.TrainerRepository;
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

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    private TrainerEntity mockTrainerEntity;
    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockTrainerEntity = new TrainerEntity();
        mockTrainerEntity.setId(1L);
        mockTrainerEntity.setFirstName("John");
        mockTrainerEntity.setLastName("Doe");
        mockTrainerEntity.setEmail("john.doe@example.com");
        mockTrainerEntity.setUsername("johndoe");

        mockTrainer = new Trainer(1L, "John", "Doe", "johndoe", "john.doe@example.com", 35, "Male", "Increase Muscle Mass");
    }

    @Test
    void getAllTrainers() {
        List<TrainerEntity> trainerEntities = Arrays.asList(mockTrainerEntity);
        when(trainerRepository.findAll()).thenReturn(trainerEntities);
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        List<Trainer> trainerList = trainerServiceImpl.getAllTrainers();

        assertNotNull(trainerList, "Trainer list should not be null.");
        assertEquals(1, trainerList.size(), "The size of the trainer list does not match.");
        assertEquals("johndoe", trainerList.get(0).getUsername(), "The trainer username does not match.");

        verify(trainerRepository, times(1)).findAll();
        verify(trainerMapper, times(1)).entityToDomain(mockTrainerEntity);
    }

    @Test
    void getTrainerById() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Trainer trainer = trainerServiceImpl.getTrainerById(1L);

        assertNotNull(trainer, "Trainer should not be null.");
        assertEquals("johndoe", trainer.getUsername(), "Trainer username does not match.");

        verify(trainerRepository, times(1)).findById(1L);
        verify(trainerMapper, times(1)).entityToDomain(mockTrainerEntity);
    }

    @Test
    void getTrainerById_TrainerNotFound() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerServiceImpl.getTrainerById(1L));

        verify(trainerRepository, times(1)).findById(1L);
        verify(trainerMapper, never()).entityToDomain(any());
    }

    @Test
    void createTrainer() {
        when(trainerMapper.domainToEntity(mockTrainer)).thenReturn(mockTrainerEntity);
        when(trainerRepository.save(mockTrainerEntity)).thenReturn(mockTrainerEntity);
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Trainer createdTrainer = trainerServiceImpl.createTrainer(mockTrainer);

        assertNotNull(createdTrainer, "Created trainer should not be null.");
        assertEquals("johndoe", createdTrainer.getUsername());

        verify(trainerMapper, times(1)).domainToEntity(mockTrainer);
        verify(trainerRepository, times(1)).save(mockTrainerEntity);
        verify(trainerMapper, times(1)).entityToDomain(mockTrainerEntity);
    }

    @Test
    void deleteTrainer() {
        when(trainerRepository.existsById(1L)).thenReturn(true);

        trainerServiceImpl.deleteTrainer(1L);

        verify(trainerRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTrainerByEmail() {
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        Trainer foundTrainer = trainerServiceImpl.getTrainerByEmail("john.doe@example.com");

        assertNotNull(foundTrainer, "Found trainer should not be null.");
        assertEquals("john.doe@example.com", foundTrainer.getEmail());

        verify(trainerRepository, times(1)).findByEmail("john.doe@example.com");
        verify(trainerMapper, times(1)).entityToDomain(mockTrainerEntity);
    }

    @Test
    void searchTrainersByPartialUsername() {
        String partialUsername = "john";
        List<TrainerEntity> trainerEntities = Arrays.asList(mockTrainerEntity);
        List<Trainer> trainerList = Arrays.asList(mockTrainer);

        when(trainerRepository.findByUsernameContainingIgnoreCase(partialUsername)).thenReturn(trainerEntities);
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        List<Trainer> result = trainerServiceImpl.searchTrainersByPartialUsername(partialUsername);

        assertEquals(1, result.size(), "Incorrect number of trainers returned.");
        assertEquals("johndoe", result.get(0).getUsername(), "Trainer username does not match expected value.");
    }

    @Test
    void updateTrainer() {
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockTrainerEntity));
        when(trainerRepository.save(mockTrainerEntity)).thenReturn(mockTrainerEntity);
        when(trainerMapper.entityToDomain(mockTrainerEntity)).thenReturn(mockTrainer);

        mockTrainer.setExpertise("Increase Stamina");

        Trainer updatedTrainer = trainerServiceImpl.updateTrainer(mockTrainer);

        assertNotNull(updatedTrainer, "Updated trainer should not be null.");
        assertEquals("Increase Stamina", updatedTrainer.getExpertise(), "Trainer expertise did not update correctly.");

        verify(trainerRepository, times(1)).findByEmail("john.doe@example.com");
        verify(trainerRepository, times(1)).save(mockTrainerEntity);
        verify(trainerMapper, times(1)).entityToDomain(mockTrainerEntity);
    }
}
