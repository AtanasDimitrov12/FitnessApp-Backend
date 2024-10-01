package fitness_app_be.fitness_app.Business.Impl;

import fitness_app_be.fitness_app.Business.WorkoutService;
import fitness_app_be.fitness_app.Domain.Workout;
import fitness_app_be.fitness_app.ExceptionHandling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.Persistence.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Override
    public List<Workout> getAllWorkouts() {
        return workoutRepository.getAll();
    }

    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.getWorkoutById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    @Override
    public Workout createWorkout(Workout workout) {
        return workoutRepository.create(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        if (!workoutRepository.exists(id)) {
            throw new WorkoutNotFoundException(id);
        }
        workoutRepository.delete(id);
    }

    @Override
    public List<Workout> searchWorkoutsByPartialUsername(String partialUsername) {
        return workoutRepository.findByNameContainingIgnoreCase(partialUsername);
    }

    @Override
    public Workout updateWorkout(Workout workout) {
        Workout existingWorkout = workoutRepository.getWorkoutById(workout.getId())
                .orElseThrow(() -> new WorkoutNotFoundException("Workout with ID " + workout.getId() + " not found"));

        // Update necessary fields
        existingWorkout.setName(workout.getName());
        existingWorkout.setDescription(workout.getDescription());
        existingWorkout.setExercises(workout.getExercises());

        return workoutRepository.update(existingWorkout);
    }
}
