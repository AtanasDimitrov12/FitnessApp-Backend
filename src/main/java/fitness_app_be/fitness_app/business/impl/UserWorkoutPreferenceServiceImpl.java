package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.UserWorkoutPreferenceService;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.exceptionHandling.UserWorkoutPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserWorkoutPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWorkoutPreferenceServiceImpl implements UserWorkoutPreferenceService {

    private final UserWorkoutPreferenceRepository userWorkoutPreferenceRepository;

    @Override
    public UserWorkoutPreference getUserWorkoutPreferenceByUserId(Long userid) {
        return userWorkoutPreferenceRepository.findByUserId(userid).orElseThrow(() -> new UserWorkoutPreferenceNotFoundException(userid));
    }

    @Override
    public UserWorkoutPreference createUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference) {
        return userWorkoutPreferenceRepository.create(userWorkoutPreference);
    }

    @Override
    public void deleteUserWorkoutPreference(Long id) {
        userWorkoutPreferenceRepository.delete(id);
    }

    @Override
    public UserWorkoutPreference updateUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference) {
        return userWorkoutPreferenceRepository.update(userWorkoutPreference);
    }
}
