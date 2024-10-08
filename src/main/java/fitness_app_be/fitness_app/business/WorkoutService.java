package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Workout;
import java.util.List;

public interface WorkoutService {
    List<Workout> getAllWorkouts();
    Workout getWorkoutById(Long id);
    Workout createWorkout(Workout workout);
    void deleteWorkout(Long id);
    List<Workout> searchWorkoutsByPartialUsername(String partialUsername);
    Workout updateWorkout(Workout workout);
}
