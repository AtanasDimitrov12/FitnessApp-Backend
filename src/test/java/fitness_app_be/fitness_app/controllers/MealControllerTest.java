package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.config.TestSecurityConfig;
import fitness_app_be.fitness_app.configuration.security.token.AccessToken;
import fitness_app_be.fitness_app.configuration.security.token.AccessTokenDecoder;
import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.controllers.mapper.MealMapper;
import fitness_app_be.fitness_app.domain.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private MealMapper mealMapper;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        // Mock AccessToken with proper email-based authentication
        AccessToken mockAccessToken = Mockito.mock(AccessToken.class);
        when(mockAccessToken.getSubject()).thenReturn("admin@example.com"); // Use email as the subject
        when(mockAccessToken.getRoles()).thenReturn(Set.of("ROLE_ADMIN"));
        when(mockAccessToken.getUserId()).thenReturn(1L);

        // Configure AccessTokenDecoder to return the mock token
        when(accessTokenDecoder.decode(Mockito.anyString())).thenReturn(mockAccessToken);

        // Reinitialize MockMvc with security applied
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void getAllMeals_ShouldReturnListOfMeals() throws Exception {
        Meal meal = new Meal(1L, "Salad", 200, 10, 15, 30.0);
        MealDTO mealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0);

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

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void getMealById_ShouldReturnMeal() throws Exception {
        Meal meal = new Meal(1L, "Salad", 200, 10, 15, 30.0);
        MealDTO mealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0);

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

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void createMeal_ShouldReturnCreatedMeal() throws Exception {
        MealDTO mealDTO = new MealDTO(null, "Salad", 200, 10, 15, 30.0);
        Meal meal = new Meal(null, "Salad", 200, 10, 15, 30.0);
        Meal createdMeal = new Meal(1L, "Salad", 200, 10, 15, 30.0);
        MealDTO createdMealDTO = new MealDTO(1L, "Salad", 200, 10, 15, 30.0);

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

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void updateMeal_ShouldReturnUpdatedMeal() throws Exception {
        MealDTO mealDTO = new MealDTO(1L, "Updated Salad", 250, 12, 18, 25.0);
        Meal meal = new Meal(1L, "Updated Salad", 250, 12, 18, 25.0);
        Meal updatedMeal = new Meal(1L, "Updated Salad", 250, 12, 18, 25.0);
        MealDTO updatedMealDTO = new MealDTO(1L, "Updated Salad", 250, 12, 18, 25.0);

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

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void deleteMeal_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(mealService).deleteMeal(1L);

        mockMvc.perform(delete("/api/meals/1"))
                .andExpect(status().isNoContent());
    }
}
