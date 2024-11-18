package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.controllers.dto.WorkoutPlanDTO;
import fitness_app_be.fitness_app.controllers.mapper.WorkoutPlanMapper;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                .toList();
    }

    @GetMapping("/{id}")
    public WorkoutPlanDTO getWorkoutPlanById(@PathVariable Long id) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        return workoutPlanMapper.domainToDto(workoutPlan);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public WorkoutPlanDTO getWorkoutPlanByUserId(@PathVariable Long userId) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanByUserId(userId);
        return workoutPlanMapper.domainToDto(workoutPlan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public WorkoutPlanDTO createWorkoutPlan(@RequestBody WorkoutPlanDTO workoutPlanDTO) {
        WorkoutPlan workoutPlan = workoutPlanMapper.toDomain(workoutPlanDTO);
        WorkoutPlan createdWorkoutPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
        return workoutPlanMapper.domainToDto(createdWorkoutPlan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public WorkoutPlanDTO updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanDTO workoutPlanDTO) {
        WorkoutPlan workoutPlan = workoutPlanMapper.toDomain(workoutPlanDTO);
        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(workoutPlan);
        return workoutPlanMapper.domainToDto(updatedWorkoutPlan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }
}
