package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.UserWorkoutPreferenceEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserWorkoutPreferenceRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserWorkoutPreferenceEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserWorkoutPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserWorkoutPreferenceRepositoryImpl implements UserWorkoutPreferenceRepository {

    private final JpaUserWorkoutPreferenceRepository jpaUserWorkoutPreferenceRepository;
    private final UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper;
    private final JpaUserRepository userRepository;

    @Autowired
    public UserWorkoutPreferenceRepositoryImpl(
            JpaUserWorkoutPreferenceRepository jpaUserWorkoutPreferenceRepository,
            UserWorkoutPreferenceEntityMapper userWorkoutPreferenceEntityMapper,
            JpaUserRepository userRepository) {
        this.jpaUserWorkoutPreferenceRepository = jpaUserWorkoutPreferenceRepository;
        this.userWorkoutPreferenceEntityMapper = userWorkoutPreferenceEntityMapper;
        this.userRepository = userRepository;
    }

    @Override
    public boolean exists(long id) {
        return jpaUserWorkoutPreferenceRepository.existsById(id);
    }

    private UserEntity findUserEntityById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    }

    @Override
    public UserWorkoutPreference create(UserWorkoutPreference preference) {
        UserEntity userEntity = findUserEntityById(preference.getUserid()); // Retrieve UserEntity
        UserWorkoutPreferenceEntity entity = userWorkoutPreferenceEntityMapper.toEntity(preference, userEntity);
        UserWorkoutPreferenceEntity savedEntity = jpaUserWorkoutPreferenceRepository.save(entity);
        return userWorkoutPreferenceEntityMapper.toDomain(savedEntity);
    }

    @Override
    public UserWorkoutPreference update(UserWorkoutPreference preference) {
        if (!exists(preference.getId())) {
            throw new IllegalArgumentException("UserWorkoutPreference with ID " + preference.getId() + " does not exist.");
        }
        UserEntity userEntity = findUserEntityById(preference.getUserid());
        UserWorkoutPreferenceEntity entity = userWorkoutPreferenceEntityMapper.toEntity(preference, userEntity);
        UserWorkoutPreferenceEntity updatedEntity = jpaUserWorkoutPreferenceRepository.save(entity);
        return userWorkoutPreferenceEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long preferenceId) {
        if (!exists(preferenceId)) {
            throw new IllegalArgumentException("UserWorkoutPreference with ID " + preferenceId + " does not exist.");
        }
        jpaUserWorkoutPreferenceRepository.deleteById(preferenceId);
    }

    @Override
    public Optional<UserWorkoutPreference> getWorkoutPreferenceById(long preferenceId) {
        return jpaUserWorkoutPreferenceRepository.findById(preferenceId)
                .map(userWorkoutPreferenceEntityMapper::toDomain);
    }

    @Override
    public Optional<UserWorkoutPreference> findByUserId(long userId) {
        return jpaUserWorkoutPreferenceRepository.findByUserId(userId)
                .map(userWorkoutPreferenceEntityMapper::toDomain);
    }
}
