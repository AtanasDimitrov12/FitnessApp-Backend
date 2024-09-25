package fitness_app_be.fitness_app.BusinessLayer.Impl;

import fitness_app_be.fitness_app.BusinessLayer.UserService;
import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.UserMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
import fitness_app_be.fitness_app.PersistenceLayer.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToDomain)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User createUser(User user) {
        UserEntity userEntity = userMapper.domainToEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.entityToDomain(savedUserEntity);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::entityToDomain)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Override
    public List<User> searchUsersByPartialUsername(String partialUsername) {
        List<UserEntity> userEntities = userRepository.findByUsernameContainingIgnoreCase(partialUsername);
        return userEntities.stream()
                .map(userMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(User user) {
        UserEntity existingUserEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + user.getEmail() + " not found"));

        existingUserEntity.setUsername(user.getUsername());
        existingUserEntity.setFitnessGoal(user.getFitnessGoal());
        existingUserEntity.setDietPreference(user.getDietPreference());

        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);
        return userMapper.entityToDomain(updatedUserEntity);
    }
}
