package fitness_app_be.fitness_app.business.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exception_handling.WorkoutNotFoundException;
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

    @SuppressWarnings("unchecked")
    String uploadImageToCloudinary(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File must not be null and must exist.");
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        Object url = uploadResult.get("url");
        if (url instanceof String urlString) {
            return urlString;
        } else {
            throw new IOException("Failed to retrieve image URL from upload result.");
        }
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
    public Workout updateWorkout(Workout workout, File imageFile) throws IOException {
        Workout existingWorkout = workoutRepository.getWorkoutById(workout.getId())
                .orElseThrow(() -> new WorkoutNotFoundException("Workout with ID " + workout.getId() + " not found"));

        String imageUrl = uploadImageToCloudinary(imageFile);
        existingWorkout.setPictureURL(imageUrl);
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
