package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userMapper;

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
    public List<User> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return jpaUserRepository.findAll(pageable).stream()
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
    public void deactivateUser(long userId) {
        jpaUserRepository.findById(userId).ifPresent(userEntity -> {
            userEntity.setIsActive(false);
            jpaUserRepository.save(userEntity);
        });
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
    public Optional<User> findByUsernameAndIsActive(String username, boolean isActive) {
        return jpaUserRepository.findByUsernameAndIsActive(username, isActive).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailAndIsActive(String email, boolean isActive) {
        return jpaUserRepository.findByEmailAndIsActive(email, isActive).map(userMapper::toDomain);
    }

    @Override
    public List<User> findByUsernameContainingIgnoreCase(String partialUsername) {
        return jpaUserRepository.findByUsernameContainingIgnoreCase(partialUsername).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByIsActive(boolean isActive) {
        return jpaUserRepository.findByIsActive(isActive).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public long countByEmail(String email) {
        return jpaUserRepository.countByEmail(email);
    }

    @Override
    public long countByIsActive(boolean isActive) {
        return jpaUserRepository.countByIsActive(isActive);
    }
}
