package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.controllers.mapper.DietMapper;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.exception_handling.JsonParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final DietMapper dietMapper;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Reuse ObjectMapper instance

    @GetMapping
    public List<DietDTO> getAllDiets() {
        return dietService.getAllDiets().stream()
                .map(dietMapper::domainToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public DietDTO getDietById(@PathVariable Long id) {
        Diet diet = dietService.getDietById(id);
        return dietMapper.domainToDto(diet);
    }

    @PostMapping(consumes = "multipart/form-data")
    public DietDTO createDiet(
            @RequestPart("diet") String dietJson,
            @RequestPart("image") MultipartFile image) {

        DietDTO dietDTO = parseDietJson(dietJson);
        Diet diet = dietMapper.toDomain(dietDTO);
        Diet createdDiet = dietService.createDiet(diet);
        return dietMapper.domainToDto(createdDiet);
    }


    @PutMapping
    public DietDTO updateDiet(@RequestBody DietDTO dietDTO) {
        Diet diet = dietMapper.toDomain(dietDTO);
        Diet updatedDiet = dietService.updateDiet(diet);
        return dietMapper.domainToDto(updatedDiet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable Long id) {
        dietService.deleteDiet(id);
        return ResponseEntity.noContent().build();
    }

    private DietDTO parseDietJson(String dietJson) {
        try {
            return objectMapper.readValue(dietJson, DietDTO.class);
        } catch (IOException e) {
            throw new JsonParsingException("Error parsing diet JSON", e);
        }
    }


}
