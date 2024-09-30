package fitness_app_be.fitness_app.Business;

import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.Domain.Workout;
import java.util.List;

public interface WorkoutService {
    List<Workout> getAllWorkouts();
    Workout getWorkoutById(Long id);
    Workout createWorkout(Workout workout);
    void deleteWorkout(Long id);
    List<Workout> searchWorkoutsByPartialUsername(String partialUsername);
    Workout updateWorkout(Workout workout);
}
