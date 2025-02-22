package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.controllers.mapper.ExerciseMapper;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<ExerciseDTO> getAllExercises() {
        return exerciseService.getAllExercises().stream()
                .map(exerciseMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Long id) {
        Exercise exercise = exerciseService.getExerciseById(id);
        return exerciseMapper.toDto(exercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ExerciseDTO createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toDomain(exerciseDTO);
        Exercise createdExercise = exerciseService.createExercise(exercise);
        return exerciseMapper.toDto(createdExercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ExerciseDTO updateExercise(@RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toDomain(exerciseDTO);
        Exercise updatedExercise = exerciseService.updateExercise(exercise);
        return exerciseMapper.toDto(updatedExercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/completed-per-muscle-group")
    public ResponseEntity<List<Map<String, Object>>> getCompletedExercisesPerMuscleGroup() {
        Map<MuscleGroup, Long> muscleGroupCounts = exerciseService.getCompletedExercisesPerMuscleGroup();

        // Transform data into a list of maps for easier consumption by the frontend
        List<Map<String, Object>> response = muscleGroupCounts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("muscleGroup", entry.getKey().getDisplayName());
                    map.put("exerciseCount", entry.getValue());
                    return map;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

}
