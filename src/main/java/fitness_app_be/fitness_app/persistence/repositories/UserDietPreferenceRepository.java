package fitness_app_be.fitness_app.persistence.repositories;


import fitness_app_be.fitness_app.domain.UserDietPreference;

import java.util.Optional;

public interface UserDietPreferenceRepository {
    boolean exists(long id);

    UserDietPreference create(UserDietPreference preference);

    UserDietPreference update(UserDietPreference preference);

    void delete(long preferenceId);

    Optional<UserDietPreference> getDietPreferenceById(long preferenceId);

    Optional<UserDietPreference> findByUserId(long userId);
}
