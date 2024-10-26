package fitness_app_be.fitness_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.controllers.mapper.MealMapper;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final MealMapper mealMapper;

    @GetMapping
    public List<MealDTO> getAllMeals() {
        return mealService.getAllMeals().stream()
                .map(mealMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MealDTO getMealById(@PathVariable Long id) {
        Meal meal = mealService.getMealById(id);
        return mealMapper.domainToDto(meal);
    }

    @PostMapping
    public MealDTO createMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = mealMapper.toDomain(mealDTO);
        Meal createdMeal = mealService.createMeal(meal);
        return mealMapper.domainToDto(createdMeal);
    }

    @PutMapping
    public MealDTO updateMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = mealMapper.toDomain(mealDTO);
        Meal updatedMeal = mealService.updateMeal(meal);
        return mealMapper.domainToDto(updatedMeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}
