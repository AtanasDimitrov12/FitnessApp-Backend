package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietPlanService;
import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.business.UserDietPreferenceService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserDietPreferenceServiceImpl implements UserDietPreferenceService {

    private final UserDietPreferenceRepository userDietPreferenceRepository;
    private final DietPlanService dietPlanService;
    private final DietService dietService;
    private final UserService userService;

    @Override
    @Transactional
    public UserDietPreference getUserDietPreferenceByUserId(Long userId) {
        return userDietPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new UserDietPreferenceNotFoundException(userId));
    }

    @Override
    @Transactional
    public UserDietPreference createUserDietPreference(UserDietPreference userDietPreference) {
        // Calculate the diet based on user preferences
        Diet calculatedDiet = dietPlanService.calculateDiet(userDietPreference);

        // Create and persist the new diet
        Diet newDiet = new Diet();
        newDiet.setMeals(calculatedDiet.getMeals());
        Diet createdDiet = dietService.createDiet(newDiet);

        // Attach the diet to the user
        User user = userService.getUserById(userDietPreference.getUser().getId());
        createdDiet.setUser(user);
        dietService.updateDiet(createdDiet);

        // Persist UserDietPreference with the associated user and diet
        userDietPreference.setUser(user);
        return userDietPreferenceRepository.create(userDietPreference);
    }

    @Override
    @Transactional
    public void deleteUserDietPreference(Long id) {
        // Validate existence before deleting
        UserDietPreference userDietPreference = userDietPreferenceRepository.getDietPreferenceById(id)
                .orElseThrow(() -> new UserDietPreferenceNotFoundException(id));

        // Delete the UserDietPreference
        userDietPreferenceRepository.delete(id);

        // Optionally: handle cascading diet cleanup if required
        // dietService.deleteDiet(userDietPreference.getDiet().getId());
    }

    @Override
    @Transactional
    public UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference) {
        // Recalculate the diet based on updated preferences
        Diet recalculatedDiet = dietPlanService.calculateDiet(userDietPreference);

        // Fetch and update the existing diet
        Diet existingDiet = dietService.getDietByUserId(userDietPreference.getUser().getId());
        for(Meal meal : existingDiet.getMeals()) {
            dietService.removeMealFromDiet(existingDiet.getId(), meal.getId());
        }
        existingDiet.setMeals(recalculatedDiet.getMeals());
        dietService.updateDiet(existingDiet);

        // Attach the updated diet to the user
        User user = userService.getUserById(userDietPreference.getUser().getId());
        existingDiet.setUser(user);
        dietService.updateDiet(existingDiet);

        // Persist the updated UserDietPreference
        userDietPreference.setUser(user);
        return userDietPreferenceRepository.update(userDietPreference);
    }
}

