package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaDietRepository;
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
    private DietEntityMapper dietEntityMapperImpl;

    @InjectMocks
    private DietRepositoryImpl dietRepository;

    private Diet diet;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        List<Meal> meals = new ArrayList<>();
        List<User> users = new ArrayList<>();

        diet = new Diet(1L, "Keto", "Low-carb diet", "picturePath", users, meals);

        dietEntity = new DietEntity();
        dietEntity.setId(1L);
        dietEntity.setName("Keto");
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
        when(dietEntityMapperImpl.toEntity(diet)).thenReturn(dietEntity);
        when(jpaDietRepository.save(dietEntity)).thenReturn(dietEntity);
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        Diet updatedDiet = dietRepository.update(diet);

        assertNotNull(updatedDiet);
        assertEquals(diet, updatedDiet);
        verify(jpaDietRepository, times(1)).save(dietEntity);
        verify(dietEntityMapperImpl, times(1)).toEntity(diet);
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }

    @Test
    void delete_ShouldDeleteDietById() {
        doNothing().when(jpaDietRepository).deleteById(1L);

        dietRepository.delete(1L);

        verify(jpaDietRepository, times(1)).deleteById(1L);
    }

    @Test
    void getDietById_ShouldReturnDiet_WhenDietExists() {
        when(jpaDietRepository.findById(1L)).thenReturn(Optional.of(dietEntity));
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        Optional<Diet> foundDiet = dietRepository.getDietById(1L);

        assertTrue(foundDiet.isPresent());
        assertEquals(diet, foundDiet.get());
        verify(jpaDietRepository, times(1)).findById(1L);
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }

    @Test
    void getDietById_ShouldReturnEmpty_WhenDietDoesNotExist() {
        when(jpaDietRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Diet> foundDiet = dietRepository.getDietById(1L);

        assertTrue(foundDiet.isEmpty());
        verify(jpaDietRepository, times(1)).findById(1L);
        verify(dietEntityMapperImpl, never()).toDomain(any());
    }

    @Test
    void findByName_ShouldReturnDiet_WhenDietWithNameExists() {
        when(jpaDietRepository.findByName("Keto")).thenReturn(Optional.of(dietEntity));
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        Optional<Diet> foundDiet = dietRepository.findByName("Keto");

        assertTrue(foundDiet.isPresent());
        assertEquals(diet, foundDiet.get());
        verify(jpaDietRepository, times(1)).findByName("Keto");
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenDietWithNameDoesNotExist() {
        when(jpaDietRepository.findByName("Keto")).thenReturn(Optional.empty());

        Optional<Diet> foundDiet = dietRepository.findByName("Keto");

        assertTrue(foundDiet.isEmpty());
        verify(jpaDietRepository, times(1)).findByName("Keto");
        verify(dietEntityMapperImpl, never()).toDomain(any());
    }

    @Test
    void findByDescriptionContainingIgnoreCase_ShouldReturnListOfDiets() {
        when(jpaDietRepository.findByDescriptionContainingIgnoreCase("low carb")).thenReturn(List.of(dietEntity));
        when(dietEntityMapperImpl.toDomain(dietEntity)).thenReturn(diet);

        List<Diet> diets = dietRepository.findByDescriptionContainingIgnoreCase("low carb");

        assertNotNull(diets);
        assertEquals(1, diets.size());
        assertEquals(diet, diets.get(0));
        verify(jpaDietRepository, times(1)).findByDescriptionContainingIgnoreCase("low carb");
        verify(dietEntityMapperImpl, times(1)).toDomain(dietEntity);
    }
}
