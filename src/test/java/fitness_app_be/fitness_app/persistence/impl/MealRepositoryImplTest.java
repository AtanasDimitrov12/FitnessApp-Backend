package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaMealRepository;
import fitness_app_be.fitness_app.persistence.mapper.MealEntityMapper;
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

class MealRepositoryImplTest {

    @Mock
    private JpaMealRepository jpaMealRepository;

    @Mock
    private MealEntityMapper mealEntityMapper;

    @InjectMocks
    private MealRepositoryImpl mealRepositoryImpl;

    private Meal mockMeal;
    private MealEntity mockMealEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMeal = new Meal(1L, 1L, "Salad", 200, 10, 5, 15.0);
        mockMealEntity = new MealEntity(1L, null, "Salad", 200, 10, 5, 15.0);
    }

    @Test
    void exists() {
        when(jpaMealRepository.existsById(1L)).thenReturn(true);

        boolean exists = mealRepositoryImpl.exists(1L);

        assertTrue(exists, "Meal should exist with ID 1.");
        verify(jpaMealRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll() {
        when(jpaMealRepository.findAll()).thenReturn(Arrays.asList(mockMealEntity));
        when(mealEntityMapper.toDomain(mockMealEntity)).thenReturn(mockMeal);

        List<Meal> meals = mealRepositoryImpl.getAll();

        assertNotNull(meals, "The list of meals should not be null.");
        assertEquals(1, meals.size(), "The number of meals returned does not match.");
        assertEquals("Salad", meals.get(0).getName(), "The meal name does not match.");

        verify(jpaMealRepository, times(1)).findAll();
        verify(mealEntityMapper, times(1)).toDomain(mockMealEntity);
    }

    @Test
    void create() {
        when(mealEntityMapper.toEntity(mockMeal)).thenReturn(mockMealEntity);
        when(jpaMealRepository.save(mockMealEntity)).thenReturn(mockMealEntity);
        when(mealEntityMapper.toDomain(mockMealEntity)).thenReturn(mockMeal);

        Meal createdMeal = mealRepositoryImpl.create(mockMeal);

        assertNotNull(createdMeal, "The created meal should not be null.");
        assertEquals("Salad", createdMeal.getName(), "The meal name does not match.");

        verify(mealEntityMapper, times(1)).toEntity(mockMeal);
        verify(jpaMealRepository, times(1)).save(mockMealEntity);
        verify(mealEntityMapper, times(1)).toDomain(mockMealEntity);
    }

    @Test
    void update() {
        when(mealEntityMapper.toEntity(mockMeal)).thenReturn(mockMealEntity);
        when(jpaMealRepository.save(mockMealEntity)).thenReturn(mockMealEntity);
        when(mealEntityMapper.toDomain(mockMealEntity)).thenReturn(mockMeal);

        mockMeal.setCalories(250);
        Meal updatedMeal = mealRepositoryImpl.update(mockMeal);

        assertNotNull(updatedMeal, "The updated meal should not be null.");
        assertEquals(250, updatedMeal.getCalories(), "The meal calories did not update correctly.");

        verify(mealEntityMapper, times(1)).toEntity(mockMeal);
        verify(jpaMealRepository, times(1)).save(mockMealEntity);
        verify(mealEntityMapper, times(1)).toDomain(mockMealEntity);
    }

    @Test
    void delete() {
        mealRepositoryImpl.delete(1L);

        verify(jpaMealRepository, times(1)).deleteById(1L);
    }

    @Test
    void getMealById() {
        when(jpaMealRepository.findById(1L)).thenReturn(Optional.of(mockMealEntity));
        when(mealEntityMapper.toDomain(mockMealEntity)).thenReturn(mockMeal);

        Optional<Meal> meal = mealRepositoryImpl.getMealById(1L);

        assertTrue(meal.isPresent(), "The meal should be present.");
        assertEquals("Salad", meal.get().getName(), "The meal name does not match.");

        verify(jpaMealRepository, times(1)).findById(1L);
        verify(mealEntityMapper, times(1)).toDomain(mockMealEntity);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        String name = "Salad";
        when(jpaMealRepository.findByNameContainingIgnoreCase(name)).thenReturn(Arrays.asList(mockMealEntity));
        when(mealEntityMapper.toDomain(mockMealEntity)).thenReturn(mockMeal);

        List<Meal> meals = mealRepositoryImpl.findByNameContainingIgnoreCase(name);

        assertNotNull(meals, "The list of meals should not be null.");
        assertEquals(1, meals.size(), "The number of meals returned does not match.");
        assertEquals("Salad", meals.get(0).getName(), "The meal name does not match.");

        verify(jpaMealRepository, times(1)).findByNameContainingIgnoreCase(name);
        verify(mealEntityMapper, times(1)).toDomain(mockMealEntity);
    }
}
