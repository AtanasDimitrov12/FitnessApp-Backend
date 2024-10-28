package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.controllers.dto.ExerciseDTO;
import fitness_app_be.fitness_app.controllers.mapper.ExerciseMapper;
import fitness_app_be.fitness_app.domain.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;

    @GetMapping
    public List<ExerciseDTO> getAllExercises() {
        return exerciseService.getAllExercises().stream()
                .map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Long id) {
        Exercise exercise = exerciseService.getExerciseById(id);
        return exerciseMapper.toDto(exercise);
    }

    @PostMapping
    public ExerciseDTO createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toDomain(exerciseDTO);
        Exercise createdExercise = exerciseService.createExercise(exercise);
        return exerciseMapper.toDto(createdExercise);
    }

    @PutMapping
    public ExerciseDTO updateExercise(@RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toDomain(exerciseDTO);
        Exercise updatedExercise = exerciseService.updateExercise(exercise);
        return exerciseMapper.toDto(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
