package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaDietRepository;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaMealRepository;
import fitness_app_be.fitness_app.persistence.mapper.DietEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietRepositoryImplTest {

    @Mock
    private JpaDietRepository jpaDietRepository;

    @Mock
    private JpaMealRepository jpaMealRepository;

    @Mock
    private DietEntityMapper dietEntityMapperImpl;

    @InjectMocks
    private DietRepositoryImpl dietRepository;

    private Diet diet;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        List<Meal> meals = new ArrayList<>();

        diet = new Diet(1L, 1L, meals);

        dietEntity = new DietEntity();
        dietEntity.setId(1L);
    }


    @Test
    void exists_ShouldReturnTrue_WhenDietExists() {
        when(jpaDietRepository.existsById(1L)).thenReturn(true);

        assertTrue(dietRepository.exists(1L));
        verify(jpaDietRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenDietDoesNotExist() {
        when(jpaDietRepository.existsById(1L)).thenReturn(false);

        assertFalse(dietRepository.exists(1L));
        verify(jpaDietRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfDiets() {
        when(jpaDietRepository.findAll()).thenReturn(List.of(dietEntity));
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        List<Diet> diets = dietRepository.getAll();

        assertNotNull(diets);
        assertEquals(1, diets.size());
        assertEquals(diet, diets.get(0));
        verify(jpaDietRepository, times(1)).findAll();
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }

    @Test
    void create_ShouldReturnCreatedDiet() {
        // Ensure the mapping and repository save are correctly mocked
        when(dietEntityMapperImpl.toEntity(diet)).thenReturn(dietEntity);
        when(jpaDietRepository.save(dietEntity)).thenReturn(dietEntity);
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        Diet createdDiet = dietRepository.create(diet);

        assertNotNull(createdDiet, "Diet should not be null after creation");
        assertEquals(diet, createdDiet);
        verify(jpaDietRepository, times(1)).save(dietEntity);
        verify(dietEntityMapperImpl, times(1)).toEntity(diet);
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }


    @Test
    void update_ShouldReturnUpdatedDiet() {
        // Arrange
        List<Meal> meals = List.of(new Meal(1L, "Meal 1", 200, 10, 20, 15.0));
        Diet inputDiet = new Diet(1L, 1L, meals); // Diet to update

        List<MealEntity> mealEntities = List.of(new MealEntity(1L, "Meal 1", 200, 10, 20, 15.0, new ArrayList<>()));
        DietEntity newdietEntity = new DietEntity(1L, 1L, new ArrayList<>(mealEntities)); // Mapped DietEntity

        when(dietEntityMapperImpl.toEntity(inputDiet)).thenReturn(newdietEntity);
        when(jpaMealRepository.findAllById(anyList())).thenReturn(mealEntities); // Mock meal fetching
        when(jpaDietRepository.save(newdietEntity)).thenReturn(newdietEntity);
        when(dietEntityMapperImpl.toDomain(newdietEntity)).thenReturn(inputDiet); // Map back to domain

        // Act
        Diet updatedDiet = dietRepository.update(inputDiet);

        // Assert
        assertNotNull(updatedDiet);
        assertEquals(inputDiet, updatedDiet);
        verify(jpaMealRepository, times(1)).findAllById(anyList());
        verify(jpaDietRepository, times(1)).save(newdietEntity);
        verify(dietEntityMapperImpl, times(1)).toDomain(newdietEntity);
        verify(dietEntityMapperImpl, times(1)).toEntity(inputDiet);
    }


    @Test
    void delete_ShouldDeleteDietById() {
        doNothing().when(jpaDietRepository).deleteById(1L);

        dietRepository.delete(1L);

        verify(jpaDietRepository, times(1)).deleteById(1L);
    }

    @Test
    void getDietById_ShouldReturnDiet_WhenDietExists() {
        // Arrange
        List<MealEntity> mealEntities = List.of(new MealEntity(1L, "Meal 1", 200, 10, 20, 15.0, new ArrayList<>()));
        DietEntity dietEntity = new DietEntity(1L, 1L, mealEntities);
        Diet expectedDiet = new Diet(1L, 1L, List.of(new Meal(1L, "Meal 1", 200, 10, 20, 15.0)));

        when(jpaDietRepository.findByIdWithMeals(1L)).thenReturn(Optional.of(dietEntity));
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(expectedDiet);

        // Act
        Optional<Diet> result = dietRepository.getDietById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedDiet, result.get());
        verify(jpaDietRepository, times(1)).findByIdWithMeals(1L);
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }


    @Test
    void getDietById_ShouldReturnEmpty_WhenDietDoesNotExist() {
        // Arrange
        when(jpaDietRepository.findByIdWithMeals(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Diet> result = dietRepository.getDietById(1L);

        // Assert
        assertTrue(result.isEmpty());
        verify(jpaDietRepository, times(1)).findByIdWithMeals(1L);
        verify(dietEntityMapperImpl, never()).toDomain(any());
    }


}
