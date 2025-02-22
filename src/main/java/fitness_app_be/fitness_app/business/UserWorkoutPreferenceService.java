package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;


public interface UserWorkoutPreferenceService {
    UserWorkoutPreference getUserWorkoutPreferenceByUserId(Long userid);
    UserWorkoutPreference createUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference);
    void deleteUserWorkoutPreference(Long id);
    UserWorkoutPreference updateUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference);
}
