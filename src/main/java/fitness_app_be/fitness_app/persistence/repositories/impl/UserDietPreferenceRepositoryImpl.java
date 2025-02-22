package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.exception_handling.UserDietPreferenceNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserDietPreferenceRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserDietPreferenceEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserDietPreferenceRepository;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDietPreferenceRepositoryImpl implements UserDietPreferenceRepository {

    private final JpaUserDietPreferenceRepository jpaUserDietPreferenceRepository;
    private final UserDietPreferenceEntityMapper userDietPreferenceEntityMapper;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    @Autowired
    public UserDietPreferenceRepositoryImpl(
            JpaUserDietPreferenceRepository jpaUserDietPreferenceRepository,
            UserDietPreferenceEntityMapper userDietPreferenceEntityMapper,
            UserRepository userRepository,  UserEntityMapper userEntityMapper) {
        this.jpaUserDietPreferenceRepository = jpaUserDietPreferenceRepository;
        this.userDietPreferenceEntityMapper = userDietPreferenceEntityMapper;
        this.userRepository = userRepository;
        this.mapper = userEntityMapper;
    }

    @Override
    public boolean exists(long id) {
        return jpaUserDietPreferenceRepository.existsById(id);
    }

    @Override
    public UserDietPreference create(UserDietPreference preference) {
        UserEntity userEntity = findUserEntityById(preference.getUserId()); // Fetch or obtain the UserEntity
        if (userEntity == null) {
            throw new UserNotFoundException( preference.getId());
        }
        UserDietPreferenceEntity entity = userDietPreferenceEntityMapper.toEntity(preference);
        UserDietPreferenceEntity savedEntity = jpaUserDietPreferenceRepository.save(entity);
        userEntity.setDietPreference(savedEntity);
        userRepository.update(mapper.toDomain(userEntity));
        return userDietPreferenceEntityMapper.toDomain(savedEntity);
    }

    @Override
    public UserDietPreference update(UserDietPreference preference) {
        // Find the existing preference by user ID
        UserDietPreferenceEntity existingEntity = jpaUserDietPreferenceRepository
                .findByUserId(preference.getUserId())
                .orElseThrow(() ->
                        new UserDietPreferenceNotFoundException(preference.getId())
                );

        // Update fields in the existing entity
        existingEntity.setCalories(preference.getCalories());
        existingEntity.setMealFrequency(preference.getMealFrequency());

        // Save the updated entity
        UserDietPreferenceEntity updatedEntity = jpaUserDietPreferenceRepository.save(existingEntity);

        // Convert the updated entity back to the domain object and return
        return userDietPreferenceEntityMapper.toDomain(updatedEntity);
    }


    @Override
    public void delete(long preferenceId) {
        if (!exists(preferenceId)) {
            throw new UserDietPreferenceNotFoundException(preferenceId);
        }
        jpaUserDietPreferenceRepository.deleteById(preferenceId);
    }

    @Override
    public Optional<UserDietPreference> getDietPreferenceById(long preferenceId) {
        return jpaUserDietPreferenceRepository.findById(preferenceId)
                .map(userDietPreferenceEntityMapper::toDomain);
    }

    @Override
    public Optional<UserDietPreference> findByUserId(long userId) {
        return jpaUserDietPreferenceRepository.findByUserId(userId)
                .map(userDietPreferenceEntityMapper::toDomain);
    }

    private UserEntity findUserEntityById(long userId) {
        return userRepository.findEntityById(userId);
    }
}
