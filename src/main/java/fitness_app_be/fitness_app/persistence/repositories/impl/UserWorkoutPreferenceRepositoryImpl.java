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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UserWorkoutPreference create(UserWorkoutPreference userWorkoutPreference) {
        // Check if a preference already exists for the user
        Optional<UserWorkoutPreferenceEntity> existingEntityOptional = jpaUserWorkoutPreferenceRepository
                .findByUserId(userWorkoutPreference.getUserid());

        if (existingEntityOptional.isPresent()) {
            throw new IllegalArgumentException(
                    "UserWorkoutPreference already exists for user ID: " + userWorkoutPreference.getUserid() +
                            ". Use update instead."
            );
        }

        // Convert domain to entity and save new preference
        UserEntity userEntity = findUserEntityById(userWorkoutPreference.getUserid());

        UserWorkoutPreferenceEntity newEntity = userWorkoutPreferenceEntityMapper.toEntity(userWorkoutPreference, userEntity);
        UserWorkoutPreferenceEntity savedEntity = jpaUserWorkoutPreferenceRepository.save(newEntity);

        userEntity.setWorkoutPreference(savedEntity);
        userRepository.save(userEntity);

        return userWorkoutPreferenceEntityMapper.toDomain(savedEntity);
    }


    @Override
    @Transactional
    public UserWorkoutPreference update(UserWorkoutPreference userWorkoutPreference) {
        // Find existing entity by user ID
        UserWorkoutPreferenceEntity existingEntity = jpaUserWorkoutPreferenceRepository
                .findByUserId(userWorkoutPreference.getUserid())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No workout preference found for user ID: " + userWorkoutPreference.getUserid()
                ));


        // Update fields in the existing entity
        existingEntity.setFitnessGoal(userWorkoutPreference.getFitnessGoal());
        existingEntity.setFitnessLevel(userWorkoutPreference.getFitnessLevel());
        existingEntity.setPreferredTrainingStyle(userWorkoutPreference.getPreferredTrainingStyle());
        existingEntity.setDaysAvailable(userWorkoutPreference.getDaysAvailable());

        // Ensure that Hibernate treats it as an update, not a new insert
        UserWorkoutPreferenceEntity updatedEntity = jpaUserWorkoutPreferenceRepository.save(existingEntity);

        // Convert and return updated domain object
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
