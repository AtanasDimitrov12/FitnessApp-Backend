package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietPlanService;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.business.UserDietPreferenceService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDietPreferenceServiceImpl implements UserDietPreferenceService {

    private final UserDietPreferenceRepository userDietPreferenceRepository;
    private final DietPlanService dietPlanService;
    private final DietService dietService;



    @Override
    public UserDietPreference getUserDietPreferenceByUserId(Long userId) {
        return userDietPreferenceRepository.findByUserId(userId).orElseThrow(() -> new UserDietPreferenceNotFoundException(userId));
    }

    @Override
    public UserDietPreference createUserDietPreference(UserDietPreference userDietPreference) {
        // Step 1: Calculate the diet
        Diet calculatedDiet = dietPlanService.calculateDiet(userDietPreference);
        // Step 2: Create and persist the diet
        Diet newDiet = new Diet();
        newDiet.setUser(userDietPreference.getUser());
        newDiet.setMeals(calculatedDiet.getMeals());
        dietService.createDiet(newDiet);

        // Step 3: Add meals
//        for (Meal meal : calculatedDiet.getMeals()) {
//            dietService.addMealToDiet(createdDiet.getId(), meal);
//        }

        // Step 4: Persist UserDietPreference
        return userDietPreferenceRepository.create(userDietPreference);
    }


    @Override
    public void deleteUserDietPreference(Long id) {
        userDietPreferenceRepository.delete(id);
    }

    @Override
    public UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference) {
        // Step 1: Recalculate the diet based on updated preferences
        Diet recalculatedDiet = dietPlanService.calculateDiet(userDietPreference);

        // Step 2: Update the existing diet
        Diet existingDiet = dietService.getDietByUserId(userDietPreference.getUser().getId());
        existingDiet.setUser(userDietPreference.getUser());
        existingDiet.setMeals(recalculatedDiet.getMeals());
        dietService.updateDiet(existingDiet);

        // Clear existing meals and add the recalculated meals
//        dietService.clearMealsFromDiet(existingDiet.getId()); // A method to clear meals
//        for (Meal meal : recalculatedDiet.getMeals()) {
//            dietService.addMealToDiet(existingDiet.getId(), meal);
//        }

        // Step 3: Persist updated UserDietPreference
        return userDietPreferenceRepository.update(userDietPreference);
    }




}
