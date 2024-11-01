package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.UserDietPreferenceService;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDietPreferenceServiceImpl implements UserDietPreferenceService {

    private final UserDietPreferenceRepository userDietPreferenceRepository;



    @Override
    public UserDietPreference getUserDietPreferenceByUserId(Long userId) {
        return userDietPreferenceRepository.findByUserId(userId).orElseThrow(() -> new UserDietPreferenceNotFoundException(userId));
    }

    @Override
    public UserDietPreference createUserDietPreference(UserDietPreference userDietPreference) {
        return userDietPreferenceRepository.create(userDietPreference);
    }

    @Override
    public void deleteUserDietPreference(Long id) {
        userDietPreferenceRepository.delete(id);
    }

    @Override
    public UserDietPreference updateUserDietPreference(UserDietPreference userDietPreference) {
        return userDietPreferenceRepository.update(userDietPreference);
    }
}
