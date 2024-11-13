package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.controllers.mapper.MealMapper;
import fitness_app_be.fitness_app.domain.Meal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MealController.class)
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private MealMapper mealMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMeals_ShouldReturnListOfMeals() throws Exception {
        Meal meal = new Meal(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());
        MealDTO mealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());

        when(mealService.getAllMeals()).thenReturn(List.of(meal));
        when(mealMapper.domainToDto(meal)).thenReturn(mealDTO);

        mockMvc.perform(get("/api/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(mealDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(mealDTO.getName())))
                .andExpect(jsonPath("$[0].calories", is(mealDTO.getCalories())))
                .andExpect(jsonPath("$[0].protein", is(mealDTO.getProtein())))
                .andExpect(jsonPath("$[0].carbs", is(mealDTO.getCarbs())))
                .andExpect(jsonPath("$[0].cookingTime", is(mealDTO.getCookingTime())));
    }

    @Test
    void getMealById_ShouldReturnMeal() throws Exception {
        Meal meal = new Meal(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());
        MealDTO mealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());

        when(mealService.getMealById(1L)).thenReturn(meal);
        when(mealMapper.domainToDto(meal)).thenReturn(mealDTO);

        mockMvc.perform(get("/api/meals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mealDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(mealDTO.getName())))
                .andExpect(jsonPath("$.calories", is(mealDTO.getCalories())))
                .andExpect(jsonPath("$.protein", is(mealDTO.getProtein())))
                .andExpect(jsonPath("$.carbs", is(mealDTO.getCarbs())))
                .andExpect(jsonPath("$.cookingTime", is(mealDTO.getCookingTime())));
    }

    @Test
    void createMeal_ShouldReturnCreatedMeal() throws Exception {
        MealDTO mealDTO = new MealDTO(null, "Salad", 200, 10, 15, 30.0, Collections.emptyList());
        Meal meal = new Meal(null, "Salad", 200, 10, 15, 30.0, Collections.emptyList());
        Meal createdMeal = new Meal(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());
        MealDTO createdMealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0, Collections.emptyList());

        when(mealMapper.toDomain(mealDTO)).thenReturn(meal);
        when(mealService.createMeal(meal)).thenReturn(createdMeal);
        when(mealMapper.domainToDto(createdMeal)).thenReturn(createdMealDTO);

        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdMealDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(createdMealDTO.getName())))
                .andExpect(jsonPath("$.calories", is(createdMealDTO.getCalories())))
                .andExpect(jsonPath("$.protein", is(createdMealDTO.getProtein())))
                .andExpect(jsonPath("$.carbs", is(createdMealDTO.getCarbs())))
                .andExpect(jsonPath("$.cookingTime", is(createdMealDTO.getCookingTime())));
    }

    @Test
    void updateMeal_ShouldReturnUpdatedMeal() throws Exception {
        MealDTO mealDTO = new MealDTO(1L, "Updated Salad", 250, 12, 18, 25.0, Collections.emptyList());
        Meal meal = new Meal(1L, "Updated Salad", 250, 12, 18, 25.0, Collections.emptyList());
        Meal updatedMeal = new Meal(1L, "Updated Salad", 250, 12, 18, 25.0, Collections.emptyList());
        MealDTO updatedMealDTO = new MealDTO(1L, "Updated Salad", 250, 12, 18, 25.0, Collections.emptyList());

        when(mealMapper.toDomain(mealDTO)).thenReturn(meal);
        when(mealService.updateMeal(meal)).thenReturn(updatedMeal);
        when(mealMapper.domainToDto(updatedMeal)).thenReturn(updatedMealDTO);

        mockMvc.perform(put("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedMealDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedMealDTO.getName())))
                .andExpect(jsonPath("$.calories", is(updatedMealDTO.getCalories())))
                .andExpect(jsonPath("$.protein", is(updatedMealDTO.getProtein())))
                .andExpect(jsonPath("$.carbs", is(updatedMealDTO.getCarbs())))
                .andExpect(jsonPath("$.cookingTime", is(updatedMealDTO.getCookingTime())));
    }

    @Test
    void deleteMeal_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(mealService).deleteMeal(1L);

        mockMvc.perform(delete("/api/meals/1"))
                .andExpect(status().isNoContent());
    }
}
