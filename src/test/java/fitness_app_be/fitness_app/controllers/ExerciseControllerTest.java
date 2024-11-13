package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.controllers.mapper.ExerciseMapper;
import fitness_app_be.fitness_app.domain.Exercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseMapper exerciseMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void deleteExercise_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(exerciseService).deleteExercise(1L);

        mockMvc.perform(delete("/api/exercises/1"))
                .andExpect(status().isNoContent());
    }
}
