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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<WorkoutDTO> getAllWorkouts() {
        return workoutService.getAllWorkouts().stream()
                .map(workoutMapper::domainToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public WorkoutDTO getWorkoutById(@PathVariable Long id) {
        Workout workout = workoutService.getWorkoutById(id);
        return workoutMapper.domainToDto(workout);
    }

    @PostMapping(consumes = "multipart/form-data")
    public WorkoutDTO createWorkout(
            @RequestPart("workout") String workoutJson,
            @RequestPart("image") MultipartFile image) {

        WorkoutDTO workoutDTO = parseWorkoutJson(workoutJson);
        Workout workout = workoutMapper.toDomain(workoutDTO);

        try {
            File file = convertMultipartFileToFile(image);
            Workout createdWorkout = workoutService.createWorkout(workout, file);
            return workoutMapper.domainToDto(createdWorkout);
        } catch (IOException e) {
            throw new CreationException("Error while creating workout", e);
        }
    }

    @PutMapping(consumes = "multipart/form-data")
    public WorkoutDTO updateWorkout(
            @RequestPart("workout") WorkoutDTO workoutDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {


            try {
                File file = convertMultipartFileToFile(image);
                Workout workout = workoutMapper.toDomain(workoutDTO);
                Workout updatedWorkout = workoutService.updateWorkout(workout, file);
                return workoutMapper.domainToDto(updatedWorkout);
            } catch (IOException e) {
                throw new CreationException("Error while saving workout image", e);
            }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    private WorkoutDTO parseWorkoutJson(String workoutJson) {
        try {
            return objectMapper.readValue(workoutJson, WorkoutDTO.class);
        } catch (IOException e) {
            throw new JsonParsingException("Error while parsing workout JSON", e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + filename);

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new FileConversionException("Error converting MultipartFile to File", e);
        }
        return convFile;
    }


}
