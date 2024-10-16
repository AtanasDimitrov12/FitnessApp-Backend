package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.persistence.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User createUser(User user) {
        return userRepository.create(user);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.exists(userId)) {
            userRepository.delete(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> searchUsersByPartialUsername(String partialUsername) {
        return userRepository.findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Override
    public User updateUser(User user) {
        if (userRepository.exists(user.getId())) {
            return userRepository.update(user);
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }


}
