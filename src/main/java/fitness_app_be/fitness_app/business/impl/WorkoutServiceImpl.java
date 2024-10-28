package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exceptionHandling.WorkoutNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final Cloudinary cloudinary;

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
    public Workout createWorkout(Workout workout, File imageFile) throws IOException {

        String imageUrl = uploadImageToCloudinary(imageFile);
        workout.setPictureURL(imageUrl);
        return workoutRepository.create(workout);
    }

    String uploadImageToCloudinary(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
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

        existingWorkout.setName(workout.getName());
        existingWorkout.setDescription(workout.getDescription());
        existingWorkout.setExercises(workout.getExercises());

        return workoutRepository.update(existingWorkout);
    }

    @Override
    public String saveImage(MultipartFile image) throws IOException {

        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        File imageFile = new File("path/to/save/images/" + filename);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(image.getBytes());
        }

        return "http://your-domain.com/images/" + filename;
    }
}
