package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.controllers.mapper.WorkoutMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @PostMapping(consumes = "multipart/form-data")
    public WorkoutDTO createWorkout(
            @RequestPart("workout") String workoutJson,  // Workout details as a JSON string
            @RequestPart("image") MultipartFile image) {    // Image file as multipart

        // Convert the workout JSON string to a WorkoutDTO object
        ObjectMapper objectMapper = new ObjectMapper();
        WorkoutDTO workoutDTO;
        try {
            workoutDTO = objectMapper.readValue(workoutJson, WorkoutDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing workout JSON", e);
        }

        // Convert MultipartFile to File
        File convertedFile = convertMultipartFileToFile(image);

        // Map DTO to domain
        Workout workout = workoutMapper.toDomain(workoutDTO);

        try {
            // Call service method to create workout with image
            Workout createdWorkout = workoutService.createWorkout(workout, convertedFile);
            return workoutMapper.domainToDto(createdWorkout);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating workout", e);
        }
    }


    @PutMapping(consumes = "multipart/form-data")
    public WorkoutDTO updateWorkout(
            @RequestPart("workout") WorkoutDTO workoutDTO,  // Workout details as JSON in the request part
            @RequestPart(value = "image", required = false) MultipartFile image) {    // Image file as multipart, optional for update

        // Handle the image file if provided
        if (image != null) {
            File convertedFile = convertMultipartFileToFile(image);
            try {
                // Handle update logic with image (similar to create logic)
                workoutService.saveImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Workout workout = workoutMapper.toDomain(workoutDTO);
        Workout updatedWorkout = workoutService.updateWorkout(workout);
        return workoutMapper.domainToDto(updatedWorkout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(convFile);
            return convFile;
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File", e);
        }
    }
}
