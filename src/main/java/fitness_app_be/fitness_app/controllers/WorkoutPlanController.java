package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.controllers.mapper.WorkoutPlanMapper;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final WorkoutPlanMapper workoutPlanMapper;

    @GetMapping
    public List<WorkoutPlanDTO> getAllWorkoutPlans() {
        return workoutPlanService.getAllWorkoutPlans().stream()
                .map(workoutPlanMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WorkoutPlanDTO getWorkoutPlanById(@PathVariable Long id) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        return workoutPlanMapper.domainToDto(workoutPlan);
    }

    @GetMapping("/user/{userId}")
    public WorkoutPlanDTO getWorkoutPlanByUserId(@PathVariable Long userId) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanByUserId(userId);
        return workoutPlanMapper.domainToDto(workoutPlan);
    }

    @PostMapping
    public WorkoutPlanDTO createWorkoutPlan(@RequestBody WorkoutPlanDTO workoutPlanDTO) {
        WorkoutPlan workoutPlan = workoutPlanMapper.toDomain(workoutPlanDTO);
        WorkoutPlan createdWorkoutPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
        return workoutPlanMapper.domainToDto(createdWorkoutPlan);
    }

    @PutMapping("/{id}")
    public WorkoutPlanDTO updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanDTO workoutPlanDTO) {
        WorkoutPlan workoutPlan = workoutPlanMapper.toDomain(workoutPlanDTO);
        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(workoutPlan);
        return workoutPlanMapper.domainToDto(updatedWorkoutPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }
}
