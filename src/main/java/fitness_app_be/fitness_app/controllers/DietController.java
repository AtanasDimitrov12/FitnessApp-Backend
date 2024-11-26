package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.controllers.mapper.DietMapper;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.exception_handling.JsonParsingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private static final Logger logger = LoggerFactory.getLogger(DietController.class);
    private final DietService dietService;
    private final DietMapper dietMapper;
    private final ObjectMapper objectMapper; // Injected via DI

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<DietDTO> getAllDiets() {
        return dietService.getAllDiets().stream()
                .map(dietMapper::domainToDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public DietDTO getDietById(@PathVariable Long id) {
        Diet diet = dietService.getDietById(id);
        return dietMapper.domainToDto(diet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public DietDTO createDiet(
            @RequestPart("diet") String dietJson,
            @RequestPart("image") MultipartFile image) {

        DietDTO dietDTO = parseDietJson(dietJson);
        Diet diet = dietMapper.toDomain(dietDTO);

        // Handle image file logic here if needed (e.g., save to storage and associate with the diet)

        Diet createdDiet = dietService.createDiet(diet);
        return dietMapper.domainToDto(createdDiet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public DietDTO updateDiet(@RequestBody DietDTO dietDTO) {
        Diet diet = dietMapper.toDomain(dietDTO);
        Diet updatedDiet = dietService.updateDiet(diet);
        return dietMapper.domainToDto(updatedDiet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable Long id) {
        dietService.deleteDiet(id);
        return ResponseEntity.noContent().build();
    }

    private DietDTO parseDietJson(String dietJson) {
        try {
            // Parse the JSON string into a DietDTO object using ObjectMapper
            return objectMapper.readValue(dietJson, DietDTO.class);
        } catch (IOException e) {
            // Log the exception with contextual information
            logger.error("Failed to parse diet JSON: {}", dietJson, e);

            // Rethrow as a custom exception with additional context
            throw new JsonParsingException("Error parsing diet JSON. Ensure the input is correctly formatted.", e);
        }
    }

}
