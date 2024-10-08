package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.controllers.mapper.WorkoutMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;

    @GetMapping
    public List<WorkoutDTO> getAllWorkouts() {
        return workoutService.getAllWorkouts().stream()
                .map(workoutMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WorkoutDTO getWorkoutById(@PathVariable Long id) {
        Workout workout = workoutService.getWorkoutById(id);
        return workoutMapper.domainToDto(workout);
    }

    @PostMapping
    public WorkoutDTO createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        Workout workout = workoutService.createWorkout(workoutMapper.toDomain(workoutDTO));
        return workoutMapper.domainToDto(workout);
    }

    @PutMapping
    public WorkoutDTO updateWorkout(@RequestBody WorkoutDTO workoutDTO) {
        Workout workout = workoutMapper.toDomain(workoutDTO);
        Workout updatedWorkout = workoutService.updateWorkout(workout);
        return workoutMapper.domainToDto(updatedWorkout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
