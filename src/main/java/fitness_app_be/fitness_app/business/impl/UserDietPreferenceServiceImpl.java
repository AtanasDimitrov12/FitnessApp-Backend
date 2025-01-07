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

import java.util.ArrayList;


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

        User user = userService.getUserById(userDietPreference.getUserId());

        Diet calculatedDiet = dietPlanService.calculateDiet(userDietPreference);

        Diet newDiet = new Diet();
        newDiet.setUserId(userDietPreference.getUserId());
        newDiet.setMeals(new ArrayList<>(calculatedDiet.getMeals()));
        Diet createdDiet = dietService.createDiet(newDiet);

        if (user.getDiet() == null || !user.getDiet().equals(createdDiet)) {
            user.setDiet(createdDiet);
            userService.updateUser(user);
        }

        userDietPreference.setUserId(user.getId());
        return userDietPreferenceRepository.create(userDietPreference);
    }



    @Override
    @Transactional
    public void deleteUserDietPreference(Long id) {
        // Check if the preference exists
        UserDietPreference existingPreference = userDietPreferenceRepository.getDietPreferenceById(id)
                .orElseThrow(() -> new UserDietPreferenceNotFoundException("Preference with ID " + id + " not found"));

        // Use the retrieved preference if needed (e.g., logging)
        userDietPreferenceRepository.delete(existingPreference.getId());
    }


    @Override
    @Transactional
    public UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference) {

        Diet recalculatedDiet = dietPlanService.calculateDiet(userDietPreference);

        Diet existingDiet = dietService.getDietByUserId(userDietPreference.getUserId());
        for(Meal meal : existingDiet.getMeals()) {
            dietService.removeMealFromDiet(existingDiet.getId(), meal.getId());
        }
        existingDiet.setUserId(userDietPreference.getUserId());
        existingDiet.setMeals(recalculatedDiet.getMeals());
        dietService.updateDiet(existingDiet);

        User user = userService.getUserById(userDietPreference.getUserId());
        dietService.updateDiet(existingDiet);

        userDietPreference.setUserId(user.getId());
        return userDietPreferenceRepository.update(userDietPreference);
    }
}

