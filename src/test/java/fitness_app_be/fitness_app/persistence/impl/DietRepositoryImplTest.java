package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaDietRepository;
import fitness_app_be.fitness_app.persistence.mapper.DietEntityMapper;
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

class DietRepositoryImplTest {

    @Mock
    private JpaDietRepository jpaDietRepository;

    @Mock
    private DietEntityMapper dietEntityMapper;

    @InjectMocks
    private DietRepositoryImpl dietRepositoryImpl;

    private Diet mockDiet;
    private DietEntity mockDietEntity;
    private TrainerEntity mockTrainerEntity;
    private List<MealEntity> mockMealEntities;
    private List<UserEntity> mockUserEntities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up the related entities
        mockTrainerEntity = new TrainerEntity(1L, "John", "Doe", "johndoe", "john.doe@example.com", 35, "Male", "Increase Muscle Mass", "./images/trainer.jpg", null, null);
        mockMealEntities = Arrays.asList(new MealEntity(1L, mockTrainerEntity, "Salad", 200, 10, 5, 15.0));
        mockUserEntities = Arrays.asList(new UserEntity(1L, "testUser", "test@example.com", "Gain muscle", "Vegetarian", "./images/user.jpg", null, null, null));

        // Setting up Diet and DietEntity with relationships
        mockDiet = new Diet(1L, 1L, "Keto Diet", "Low-carb, high-fat diet", "./images/diet.jpg", null, null);
        mockDietEntity = new DietEntity(1L, mockTrainerEntity, "Keto Diet", "Low-carb, high-fat diet", "./images/diet.jpg", mockMealEntities, mockUserEntities);
    }

    @Test
    void exists() {
        when(jpaDietRepository.existsById(1L)).thenReturn(true);

        boolean exists = dietRepositoryImpl.exists(1L);

        assertTrue(exists, "Diet should exist with ID 1.");
        verify(jpaDietRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaDietRepository.findAll()).thenReturn(Arrays.asList(mockDietEntity));
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        List<Diet> diets = dietRepositoryImpl.getAll();

        assertNotNull(diets, "The list of diets should not be null.");
        assertEquals(1, diets.size(), "The number of diets returned does not match.");
        assertEquals("Keto Diet", diets.get(0).getName(), "The diet name does not match.");

        verify(jpaDietRepository, times(1)).findAll();
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }

    @Test
    void create() {
        when(dietEntityMapper.toEntity(mockDiet)).thenReturn(mockDietEntity);
        when(jpaDietRepository.save(mockDietEntity)).thenReturn(mockDietEntity);
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        Diet createdDiet = dietRepositoryImpl.create(mockDiet);

        assertNotNull(createdDiet, "The created diet should not be null.");
        assertEquals("Keto Diet", createdDiet.getName(), "The diet name does not match.");

        verify(dietEntityMapper, times(1)).toEntity(mockDiet);
        verify(jpaDietRepository, times(1)).save(mockDietEntity);
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }

    @Test
    void update() {
        when(dietEntityMapper.toEntity(mockDiet)).thenReturn(mockDietEntity);
        when(jpaDietRepository.save(mockDietEntity)).thenReturn(mockDietEntity);
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        mockDiet.setDescription("Updated Diet Description");
        Diet updatedDiet = dietRepositoryImpl.update(mockDiet);

        assertNotNull(updatedDiet, "The updated diet should not be null.");
        assertEquals("Updated Diet Description", updatedDiet.getDescription(), "The diet description did not update correctly.");

        verify(dietEntityMapper, times(1)).toEntity(mockDiet);
        verify(jpaDietRepository, times(1)).save(mockDietEntity);
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }

    @Test
    void delete() {
        dietRepositoryImpl.delete(1L);

        verify(jpaDietRepository, times(1)).deleteById(1L);
    }

    @Test
    void getDietById() {
        when(jpaDietRepository.findById(1L)).thenReturn(Optional.of(mockDietEntity));
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        Optional<Diet> diet = dietRepositoryImpl.getDietById(1L);

        assertTrue(diet.isPresent(), "The diet should be present.");
        assertEquals("Keto Diet", diet.get().getName(), "The diet name does not match.");

        verify(jpaDietRepository, times(1)).findById(1L);
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }

    @Test
    void findByName() {
        when(jpaDietRepository.findByName("Keto Diet")).thenReturn(Optional.of(mockDietEntity));
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        Optional<Diet> diet = dietRepositoryImpl.findByName("Keto Diet");

        assertTrue(diet.isPresent(), "The diet should be present.");
        assertEquals("Keto Diet", diet.get().getName(), "The diet name does not match.");

        verify(jpaDietRepository, times(1)).findByName("Keto Diet");
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }

    @Test
    void findByDescriptionContainingIgnoreCase() {
        String description = "low-carb";
        when(jpaDietRepository.findByDescriptionContainingIgnoreCase(description)).thenReturn(Arrays.asList(mockDietEntity));
        when(dietEntityMapper.toDomain(mockDietEntity)).thenReturn(mockDiet);

        List<Diet> diets = dietRepositoryImpl.findByDescriptionContainingIgnoreCase(description);

        assertNotNull(diets, "The list of diets should not be null.");
        assertEquals(1, diets.size(), "The number of diets returned does not match.");
        assertEquals("Keto Diet", diets.get(0).getName(), "The diet name does not match.");

        verify(jpaDietRepository, times(1)).findByDescriptionContainingIgnoreCase(description);
        verify(dietEntityMapper, times(1)).toDomain(mockDietEntity);
    }
}
