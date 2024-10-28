package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.controllers.mapper.UserMapper;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaUserDietPreferenceRepository;
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
    private final UserEntityMapper userEntityMapper;

    @Autowired
    public UserDietPreferenceRepositoryImpl(
            JpaUserDietPreferenceRepository jpaUserDietPreferenceRepository,
            UserDietPreferenceEntityMapper userDietPreferenceEntityMapper,
            UserRepository userRepository,  UserEntityMapper userEntityMapper) {
        this.jpaUserDietPreferenceRepository = jpaUserDietPreferenceRepository;
        this.userDietPreferenceEntityMapper = userDietPreferenceEntityMapper;
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public boolean exists(long id) {
        return jpaUserDietPreferenceRepository.existsById(id);
    }

    @Override
    public UserDietPreference create(UserDietPreference preference) {
        UserEntity userEntity = findUserEntityById(preference.getUserid()); // Fetch or obtain the UserEntity
        UserDietPreferenceEntity entity = userDietPreferenceEntityMapper.toEntity(preference, userEntity);
        UserDietPreferenceEntity savedEntity = jpaUserDietPreferenceRepository.save(entity);
        return userDietPreferenceEntityMapper.toDomain(savedEntity);
    }

    @Override
    public UserDietPreference update(UserDietPreference preference) {
        if (!exists(preference.getId())) {
            throw new IllegalArgumentException("UserDietPreference with ID " + preference.getId() + " does not exist.");
        }
        UserEntity userEntity = findUserEntityById(preference.getUserid()); // Fetch or obtain the UserEntity
        UserDietPreferenceEntity entity = userDietPreferenceEntityMapper.toEntity(preference, userEntity);
        UserDietPreferenceEntity updatedEntity = jpaUserDietPreferenceRepository.save(entity);
        return userDietPreferenceEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long preferenceId) {
        if (!exists(preferenceId)) {
            throw new IllegalArgumentException("UserDietPreference with ID " + preferenceId + " does not exist.");
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
        // Implement logic to fetch UserEntity by userId from UserRepository or similar source
        // For example:
        return userEntityMapper.toEntity(userRepository.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found.")));
    }
}
