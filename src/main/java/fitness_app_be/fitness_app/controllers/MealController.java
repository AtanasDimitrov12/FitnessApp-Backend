package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.MealService;
import fitness_app_be.fitness_app.controllers.dto.MealDTO;
import fitness_app_be.fitness_app.controllers.mapper.MealMapper;
import fitness_app_be.fitness_app.domain.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final MealMapper mealMapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<MealDTO> getAllMeals() {
        return mealService.getAllMeals().stream()
                .map(mealMapper::domainToDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public MealDTO getMealById(@PathVariable Long id) {
        Meal meal = mealService.getMealById(id);
        return mealMapper.domainToDto(meal);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public MealDTO createMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = mealMapper.toDomain(mealDTO);
        Meal createdMeal = mealService.createMeal(meal);
        return mealMapper.domainToDto(createdMeal);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public MealDTO updateMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = mealMapper.toDomain(mealDTO);
        Meal updatedMeal = mealService.updateMeal(meal);
        return mealMapper.domainToDto(updatedMeal);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}
