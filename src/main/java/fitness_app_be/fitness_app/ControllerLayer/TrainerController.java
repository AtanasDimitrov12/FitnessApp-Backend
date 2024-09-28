package fitness_app_be.fitness_app.ControllerLayer;


import fitness_app_be.fitness_app.BusinessLayer.TrainerService;
import fitness_app_be.fitness_app.ControllerLayer.DTOs.TrainerDTO;
import fitness_app_be.fitness_app.ControllerLayer.DTOs.UserDTO;
import fitness_app_be.fitness_app.Domain.Trainer;
import fitness_app_be.fitness_app.MapperLayer.TrainerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    @GetMapping
    public List<TrainerDTO> getAllTrainers() {

        return trainerService.getAllTrainers().stream()
                .map(trainerMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TrainerDTO getTrainerById(@PathVariable Long id) {

        Trainer trainer = trainerService.getTrainerById(id);
        return trainerMapper.domainToDto(trainer);
    }

    @PostMapping
    public TrainerDTO createTrainer(@RequestBody TrainerDTO trainerDTO) {

        Trainer trainer = trainerService.createTrainer(trainerMapper.toDomain(trainerDTO));
        return trainerMapper.domainToDto(trainer);
    }

    @PutMapping
    public TrainerDTO updateTrainer(@RequestBody TrainerDTO trainerDTO) {

        Trainer trainer = trainerMapper.toDomain(trainerDTO);
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);
        return trainerMapper.domainToDto(updatedTrainer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }
}
