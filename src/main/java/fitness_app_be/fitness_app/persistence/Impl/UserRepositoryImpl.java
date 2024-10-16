package fitness_app_be.fitness_app.persistence.Impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.Entity.UserEntity;
import fitness_app_be.fitness_app.persistence.JPARepositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.Mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.Repositories.UserRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userMapper;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, UserEntityMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean exists(long id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public List<User> getAll() {
        return jpaUserRepository.findAll().stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public User create(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public User update(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity updatedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long userId) {
        jpaUserRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return jpaUserRepository.findById(userId).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username).map(userMapper::toDomain);
    }

    @Override
    public List<User> findByFitnessGoal(String fitnessGoal) {
        return jpaUserRepository.findByFitnessGoal(fitnessGoal).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByUsernameContainingIgnoreCase(String partialUsername) {
        return jpaUserRepository.findByUsernameContainingIgnoreCase(partialUsername).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public long countByEmail(String email) {
        return jpaUserRepository.countByEmail(email);
    }
}

