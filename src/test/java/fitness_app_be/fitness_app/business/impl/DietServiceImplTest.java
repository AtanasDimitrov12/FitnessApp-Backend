package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.exceptionHandling.DietNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.DietRepository;
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

class DietServiceImplTest {

    @Mock
    private DietRepository dietRepository;

    @InjectMocks
    private DietServiceImpl dietServiceImpl;

    private Diet mockDiet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDiet = new Diet(1L, 2L, "Vegan Diet", "A diet with plant-based meals", "./images/diet.jpg", Arrays.asList(), Arrays.asList());
    }

    @Test
    void getAllDiets() {
        List<Diet> diets = Arrays.asList(mockDiet);
        when(dietRepository.getAll()).thenReturn(diets);

        List<Diet> result = dietServiceImpl.getAllDiets();

        assertNotNull(result, "The list of diets should not be null.");
        assertEquals(1, result.size(), "The size of the diet list does not match.");
        assertEquals("Vegan Diet", result.get(0).getName(), "The diet name does not match.");

        verify(dietRepository, times(1)).getAll();
    }

    @Test
    void getDietById() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.of(mockDiet));

        Diet diet = dietServiceImpl.getDietById(1L);

        assertNotNull(diet, "The diet should not be null.");
        assertEquals("Vegan Diet", diet.getName(), "The diet name does not match.");

        verify(dietRepository, times(1)).getDietById(1L);
    }

    @Test
    void getDietById_DietNotFound() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.empty());

        assertThrows(DietNotFoundException.class, () -> dietServiceImpl.getDietById(1L), "Expected DietNotFoundException");

        verify(dietRepository, times(1)).getDietById(1L);
    }

    @Test
    void createDiet() {
        when(dietRepository.create(mockDiet)).thenReturn(mockDiet);

        Diet createdDiet = dietServiceImpl.createDiet(mockDiet);

        assertNotNull(createdDiet, "The created diet should not be null.");
        assertEquals("Vegan Diet", createdDiet.getName(), "The diet name does not match.");

        verify(dietRepository, times(1)).create(mockDiet);
    }

    @Test
    void deleteDiet() {
        when(dietRepository.exists(1L)).thenReturn(true);

        dietServiceImpl.deleteDiet(1L);

        verify(dietRepository, times(1)).delete(1L);
    }

    @Test
    void deleteDiet_DietNotFound() {
        when(dietRepository.exists(1L)).thenReturn(false);

        assertThrows(DietNotFoundException.class, () -> dietServiceImpl.deleteDiet(1L), "Expected DietNotFoundException");

        verify(dietRepository, times(1)).exists(1L);
        verify(dietRepository, never()).delete(1L);
    }

    @Test
    void updateDiet() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.of(mockDiet));
        when(dietRepository.update(mockDiet)).thenReturn(mockDiet);

        mockDiet.setName("Updated Vegan Diet");

        Diet updatedDiet = dietServiceImpl.updateDiet(mockDiet);

        assertNotNull(updatedDiet, "The updated diet should not be null.");
        assertEquals("Updated Vegan Diet", updatedDiet.getName(), "The diet name was not updated correctly.");

        verify(dietRepository, times(1)).getDietById(1L);
        verify(dietRepository, times(1)).update(mockDiet);
    }

    @Test
    void updateDiet_DietNotFound() {
        when(dietRepository.getDietById(1L)).thenReturn(Optional.empty());

        mockDiet.setName("Updated Vegan Diet");

        assertThrows(DietNotFoundException.class, () -> dietServiceImpl.updateDiet(mockDiet), "Expected DietNotFoundException");

        verify(dietRepository, times(1)).getDietById(1L);
        verify(dietRepository, never()).update(mockDiet);
    }
}
