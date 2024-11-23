package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.config.TestSecurityConfig;
import fitness_app_be.fitness_app.configuration.security.token.AccessToken;
import fitness_app_be.fitness_app.configuration.security.token.AccessTokenDecoder;
import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.controllers.mapper.ExerciseMapper;
import fitness_app_be.fitness_app.domain.Exercise;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseMapper exerciseMapper;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        // Mock AccessToken with email-based authentication and roles
        AccessToken mockAccessToken = Mockito.mock(AccessToken.class);
        when(mockAccessToken.getSubject()).thenReturn("admin@example.com"); // Email as the subject
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
    void getAllExercises_ShouldReturnListOfExercises() throws Exception {
        Exercise exercise = new Exercise(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());
        ExerciseDTO exerciseDTO = new ExerciseDTO(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());

        when(exerciseService.getAllExercises()).thenReturn(List.of(exercise));
        when(exerciseMapper.toDto(exercise)).thenReturn(exerciseDTO);

        mockMvc.perform(get("/api/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(exerciseDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(exerciseDTO.getName())))
                .andExpect(jsonPath("$[0].sets", is(exerciseDTO.getSets())))
                .andExpect(jsonPath("$[0].reps", is(exerciseDTO.getReps())))
                .andExpect(jsonPath("$[0].muscleGroup", is(exerciseDTO.getMuscleGroup())));
    }

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void getExerciseById_ShouldReturnExercise() throws Exception {
        Exercise exercise = new Exercise(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());
        ExerciseDTO exerciseDTO = new ExerciseDTO(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());

        when(exerciseService.getExerciseById(1L)).thenReturn(exercise);
        when(exerciseMapper.toDto(exercise)).thenReturn(exerciseDTO);

        mockMvc.perform(get("/api/exercises/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(exerciseDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(exerciseDTO.getName())))
                .andExpect(jsonPath("$.sets", is(exerciseDTO.getSets())))
                .andExpect(jsonPath("$.reps", is(exerciseDTO.getReps())))
                .andExpect(jsonPath("$.muscleGroup", is(exerciseDTO.getMuscleGroup())));
    }

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void createExercise_ShouldReturnCreatedExercise() throws Exception {
        ExerciseDTO exerciseDTO = new ExerciseDTO(null, "Push Up", 3, 15, "Chest", Collections.emptyList());
        Exercise exercise = new Exercise(null, "Push Up", 3, 15, "Chest", Collections.emptyList());
        Exercise createdExercise = new Exercise(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());
        ExerciseDTO createdExerciseDTO = new ExerciseDTO(1L, "Push Up", 3, 15, "Chest", Collections.emptyList());

        when(exerciseMapper.toDomain(exerciseDTO)).thenReturn(exercise);
        when(exerciseService.createExercise(exercise)).thenReturn(createdExercise);
        when(exerciseMapper.toDto(createdExercise)).thenReturn(createdExerciseDTO);

        mockMvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdExerciseDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(createdExerciseDTO.getName())))
                .andExpect(jsonPath("$.sets", is(createdExerciseDTO.getSets())))
                .andExpect(jsonPath("$.reps", is(createdExerciseDTO.getReps())))
                .andExpect(jsonPath("$.muscleGroup", is(createdExerciseDTO.getMuscleGroup())));
    }

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void updateExercise_ShouldReturnUpdatedExercise() throws Exception {
        ExerciseDTO exerciseDTO = new ExerciseDTO(1L, "Updated Push Up", 4, 12, "Upper Body", Collections.emptyList());
        Exercise exercise = new Exercise(1L, "Updated Push Up", 4, 12, "Upper Body", Collections.emptyList());
        Exercise updatedExercise = new Exercise(1L, "Updated Push Up", 4, 12, "Upper Body", Collections.emptyList());
        ExerciseDTO updatedExerciseDTO = new ExerciseDTO(1L, "Updated Push Up", 4, 12, "Upper Body", Collections.emptyList());

        when(exerciseMapper.toDomain(exerciseDTO)).thenReturn(exercise);
        when(exerciseService.updateExercise(exercise)).thenReturn(updatedExercise);
        when(exerciseMapper.toDto(updatedExercise)).thenReturn(updatedExerciseDTO);

        mockMvc.perform(put("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedExerciseDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedExerciseDTO.getName())))
                .andExpect(jsonPath("$.sets", is(updatedExerciseDTO.getSets())))
                .andExpect(jsonPath("$.reps", is(updatedExerciseDTO.getReps())))
                .andExpect(jsonPath("$.muscleGroup", is(updatedExerciseDTO.getMuscleGroup())));
    }

    @WithMockUser(username = "admin@example.com", authorities = "ROLE_ADMIN")
    @Test
    void deleteExercise_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(exerciseService).deleteExercise(1L);

        mockMvc.perform(delete("/api/exercises/1"))
                .andExpect(status().isNoContent());
    }
}
