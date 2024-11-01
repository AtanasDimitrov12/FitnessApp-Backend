package fitness_app_be.fitness_app.persistence.repositories;


import fitness_app_be.fitness_app.domain.UserWorkoutPreference;

import java.util.Optional;

public interface UserWorkoutPreferenceRepository {
    boolean exists(long id);

    UserWorkoutPreference create(UserWorkoutPreference preference);

    UserWorkoutPreference update(UserWorkoutPreference preference);

    void delete(long preferenceId);

    Optional<UserWorkoutPreference> getWorkoutPreferenceById(long preferenceId);

    Optional<UserWorkoutPreference> findByUserId(long userId);
}
