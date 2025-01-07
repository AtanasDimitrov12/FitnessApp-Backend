package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.controllers.dto.WorkoutDTO;
import fitness_app_be.fitness_app.controllers.mapper.WorkoutMapper;
import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.exception_handling.CreationException;
import fitness_app_be.fitness_app.exception_handling.FileConversionException;
import fitness_app_be.fitness_app.exception_handling.JsonParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Slf4j
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<WorkoutDTO> getAllWorkouts() {
        log.info("Fetching all workouts.");
        return workoutService.getAllWorkouts().stream()
                .map(workoutMapper::domainToDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public WorkoutDTO getWorkoutById(@PathVariable Long id) {
        log.info("Fetching workout with ID: {}", id);
        Workout workout = workoutService.getWorkoutById(id);
        return workoutMapper.domainToDto(workout);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public WorkoutDTO createWorkout(
            @RequestPart("workout") String workoutJson,
            @RequestPart("image") MultipartFile image) {

        log.info("Creating a new workout.");
        WorkoutDTO workoutDTO = parseWorkoutJson(workoutJson);
        Workout workout = workoutMapper.toDomain(workoutDTO);
        try {
            File file = convertMultipartFileToFile(image);
            Workout createdWorkout = workoutService.createWorkout(workout, file);
            log.info("Workout created successfully with ID: {}", createdWorkout.getId());
            return workoutMapper.domainToDto(createdWorkout);
        } catch (IOException e) {
            log.error("Error while creating workout.", e);
            throw new CreationException("Error while creating workout", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = "multipart/form-data")
    public WorkoutDTO updateWorkout(
            @RequestPart("workout") String workoutDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        File file = null; // Initialize the file outside try block for cleanup in finally
        try {
            log.info("Updating workout.");

            // Deserialize the workoutDTOJson to WorkoutDTO
            WorkoutDTO workoutDTO = objectMapper.readValue(workoutDTOJson, WorkoutDTO.class);

            // Convert the MultipartFile to a File, if image is provided
            if (image != null) {
                file = convertMultipartFileToFile(image);
            }

            // Map DTO to domain and update the workout
            Workout workout = workoutMapper.toDomain(workoutDTO);
            Workout updatedWorkout = workoutService.updateWorkout(workout, file);

            // Send a notification message to the topic
            String message = "Workout \"" + updatedWorkout.getName() + "\" has been updated by the admin.";
            messagingTemplate.convertAndSend("/topic/workouts/" + updatedWorkout.getId(), message);

            log.info("Workout updated successfully with ID: {}", updatedWorkout.getId());

            // Map the updated domain object back to DTO
            return workoutMapper.domainToDto(updatedWorkout);

        } catch (IOException e) {
            log.error("Error while updating workout.", e);
            throw new CreationException("Error while saving workout image", e);
        } finally {
            // Clean up temporary file
            if (file != null && file.exists()) {
                try {
                    Files.delete(file.toPath());
                    log.info("Temporary file deleted: {}", file.getAbsolutePath());
                } catch (IOException deleteException) {
                    log.error("Failed to delete temporary file: {}", file.getAbsolutePath(), deleteException);
                }
            }
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/basic")
    public WorkoutDTO updateWorkoutWithoutPicture(@RequestBody WorkoutDTO workoutDTO) {
        log.info("Updating workout without picture.");
        Workout workout = workoutMapper.toDomain(workoutDTO);
        Workout updatedWorkout = workoutService.updateWorkoutWithoutPicture(workout);
        String message = "Workout \"" + updatedWorkout.getName() + "\" has been updated by the admin.";
        messagingTemplate.convertAndSend("/topic/workouts/" + updatedWorkout.getId(), message);

        log.info("Workout updated successfully without picture. ID: {}", updatedWorkout.getId());
        return workoutMapper.domainToDto(updatedWorkout);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        log.info("Deleting workout with ID: {}", id);
        workoutService.deleteWorkout(id);
        log.info("Workout deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    private WorkoutDTO parseWorkoutJson(String workoutJson) {
        try {
            return objectMapper.readValue(workoutJson, WorkoutDTO.class);
        } catch (IOException e) {
            log.error("Error while parsing workout JSON.", e);
            throw new JsonParsingException("Error while parsing workout JSON", e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            log.warn("No file uploaded or file is empty.");
            throw new FileConversionException("No file uploaded or file is empty");
        }

        String filename = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + filename);

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
            log.info("MultipartFile converted to File: {}", convFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error converting MultipartFile to File.", e);
            throw new FileConversionException("Error converting MultipartFile to File", e);
        }
        return convFile;
    }
}
