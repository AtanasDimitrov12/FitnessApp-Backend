package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutPlanService;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Override
    public List<WorkoutPlan> getAllWorkoutPlans(){
        return workoutPlanRepository.getAll();
    }

    @Override
    public WorkoutPlan getWorkoutPlanById(Long id) {
        return workoutPlanRepository.getWorkoutPlanById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(id));
    }

    @Override
    public WorkoutPlan getWorkoutPlanByUserId(Long userId) {
        return workoutPlanRepository.getWorkoutPlanByUserId(userId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(userId));
    }

    @Override
    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.create(workoutPlan);
    }

    @Override
    public void deleteWorkoutPlan(Long id) {
        if (!workoutPlanRepository.exists(id)) {
            throw new WorkoutPlanNotFoundException(id);
        }
        workoutPlanRepository.delete(id);
    }

    @Override
    public WorkoutPlan updateWorkoutPlan(WorkoutPlan workoutPlan) {
        WorkoutPlan existingPlan = workoutPlanRepository.getWorkoutPlanById(workoutPlan.getId())
                .orElseThrow(() -> new WorkoutPlanNotFoundException(workoutPlan.getId()));

        existingPlan.setWorkouts(workoutPlan.getWorkouts());
        return workoutPlanRepository.update(existingPlan);
    }
}
