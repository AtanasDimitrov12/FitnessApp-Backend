package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Workout;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.io.IOException;

public interface WorkoutService {
    List<Workout> getAllWorkouts();
    Workout getWorkoutById(Long id);
    Workout createWorkout(Workout workout, File imageFile) throws IOException;
    void deleteWorkout(Long id);
    List<Workout> searchWorkoutsByPartialUsername(String partialUsername);
    Workout updateWorkout(Workout workout, File imageFile) throws IOException;
    String saveImage(MultipartFile image) throws IOException;
}
