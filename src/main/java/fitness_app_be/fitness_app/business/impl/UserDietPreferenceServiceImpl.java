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

    public UserDietPreference createUserDietPreference(UserDietPreference userDietPreference) {

        Diet userDiet = dietPlanService.calculateDiet(userDietPreference);
        userDiet.setUser(userDietPreference.getUser());

        dietService.createDiet(userDiet);



        return userDietPreferenceRepository.create(userDietPreference);
    }


    @Override
    public void deleteUserDietPreference(Long id) {
        userDietPreferenceRepository.delete(id);
    }

    @Override
    public UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference) {
        // Update the user's diet preference
        UserDietPreference updatedDietPreference = userDietPreferenceRepository.update(userDietPreference);

        // Recalculate the diet based on updated preferences
        Diet oldDiet = dietService.getDietByUserId(updatedDietPreference.getUser().getId());
        Diet userDiet = dietPlanService.calculateDiet(updatedDietPreference);
        Diet newDiet = new Diet(oldDiet.getId(), updatedDietPreference.getUser(), userDiet.getMeals());

        dietService.updateDiet(newDiet);

        return updatedDietPreference;
    }



}
