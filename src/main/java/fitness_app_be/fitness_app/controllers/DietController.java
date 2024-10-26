package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.controllers.mapper.DietMapper;
import fitness_app_be.fitness_app.domain.Diet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final DietMapper dietMapper;

    @GetMapping
    public List<DietDTO> getAllDiets() {
        return dietService.getAllDiets().stream()
                .map(dietMapper::domainToDto)
                .collect(Collectors.toList());
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

        ObjectMapper objectMapper = new ObjectMapper();
        DietDTO dietDTO;
        try {
            dietDTO = objectMapper.readValue(dietJson, DietDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing diet JSON", e);
        }

        File convertedFile = convertMultipartFileToFile(image);
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
