package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.UserDietPreference;

import java.util.List;

public interface UserDietPreferenceService {
    UserDietPreference getUserDietPreferenceByUserId(Long userid);
    UserDietPreference createUserDietPreference(UserDietPreference userDietPreference);
    void deleteUserDietPreference(Long id);
    UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference);
}
