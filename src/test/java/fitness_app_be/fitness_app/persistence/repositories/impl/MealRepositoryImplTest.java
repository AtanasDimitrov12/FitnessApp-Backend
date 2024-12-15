package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaMealRepository;
import fitness_app_be.fitness_app.persistence.mapper.MealEntityMapper;
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
class MealRepositoryImplTest {

    @Mock
    private JpaMealRepository jpaMealRepository;

    @Mock
    private MealEntityMapper mealEntityMapperImpl;

    @InjectMocks
    private MealRepositoryImpl mealRepository;

    private Meal meal;
    private MealEntity mealEntity;

    @BeforeEach
    void setUp() {
        List<Diet> diets = new ArrayList<>();
        meal = new Meal(1L, "Chicken Salad", 300, 25, 10, 15);


        mealEntity = new MealEntity();
        mealEntity.setId(1L);
        mealEntity.setName("Salad");
    }

    @Test
    void exists_ShouldReturnTrue_WhenMealExists() {
        when(jpaMealRepository.existsById(1L)).thenReturn(true);

        assertTrue(mealRepository.exists(1L));
        verify(jpaMealRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenMealDoesNotExist() {
        when(jpaMealRepository.existsById(1L)).thenReturn(false);

        assertFalse(mealRepository.exists(1L));
        verify(jpaMealRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfMeals() {
        when(jpaMealRepository.findAll()).thenReturn(List.of(mealEntity));
        when(mealEntityMapperImpl.toDomain(mealEntity)).thenReturn(meal);

        List<Meal> meals = mealRepository.getAll();

        assertNotNull(meals);
        assertEquals(1, meals.size());
        assertEquals(meal, meals.get(0));
        verify(jpaMealRepository, times(1)).findAll();
        verify(mealEntityMapperImpl, times(1)).toDomain(mealEntity);
    }

    @Test
    void create_ShouldReturnCreatedMeal() {
        when(mealEntityMapperImpl.toEntity(meal)).thenReturn(mealEntity);
        when(jpaMealRepository.save(mealEntity)).thenReturn(mealEntity);
        when(mealEntityMapperImpl.toDomain(mealEntity)).thenReturn(meal);

        Meal createdMeal = mealRepository.create(meal);

        assertNotNull(createdMeal);
        assertEquals(meal, createdMeal);
        verify(jpaMealRepository, times(1)).save(mealEntity);
        verify(mealEntityMapperImpl, times(1)).toEntity(meal);
        verify(mealEntityMapperImpl, times(1)).toDomain(mealEntity);
    }

    @Test
    void update_ShouldReturnUpdatedMeal() {
        when(mealEntityMapperImpl.toEntity(meal)).thenReturn(mealEntity);
        when(jpaMealRepository.save(mealEntity)).thenReturn(mealEntity);
        when(mealEntityMapperImpl.toDomain(mealEntity)).thenReturn(meal);

        Meal updatedMeal = mealRepository.update(meal);

        assertNotNull(updatedMeal);
        assertEquals(meal, updatedMeal);
        verify(jpaMealRepository, times(1)).save(mealEntity);
        verify(mealEntityMapperImpl, times(1)).toEntity(meal);
        verify(mealEntityMapperImpl, times(1)).toDomain(mealEntity);
    }

    @Test
    void delete_ShouldDeleteMealById() {
        doNothing().when(jpaMealRepository).deleteById(1L);

        mealRepository.delete(1L);

        verify(jpaMealRepository, times(1)).deleteById(1L);
    }

    @Test
    void getMealById_ShouldReturnMeal_WhenMealExists() {
        when(jpaMealRepository.findById(1L)).thenReturn(Optional.of(mealEntity));
        when(mealEntityMapperImpl.toDomain(mealEntity)).thenReturn(meal);

        Optional<Meal> foundMeal = mealRepository.getMealById(1L);

        assertTrue(foundMeal.isPresent());
        assertEquals(meal, foundMeal.get());
        verify(jpaMealRepository, times(1)).findById(1L);
        verify(mealEntityMapperImpl, times(1)).toDomain(mealEntity);
    }

    @Test
    void getMealById_ShouldReturnEmpty_WhenMealDoesNotExist() {
        when(jpaMealRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Meal> foundMeal = mealRepository.getMealById(1L);

        assertTrue(foundMeal.isEmpty());
        verify(jpaMealRepository, times(1)).findById(1L);
        verify(mealEntityMapperImpl, never()).toDomain(any());
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnListOfMeals() {
        when(jpaMealRepository.findByNameContainingIgnoreCase("salad")).thenReturn(List.of(mealEntity));
        when(mealEntityMapperImpl.toDomain(mealEntity)).thenReturn(meal);

        List<Meal> meals = mealRepository.findByNameContainingIgnoreCase("salad");

        assertNotNull(meals);
        assertEquals(1, meals.size());
        assertEquals(meal, meals.get(0));
        verify(jpaMealRepository, times(1)).findByNameContainingIgnoreCase("salad");
        verify(mealEntityMapperImpl, times(1)).toDomain(mealEntity);
    }
}
