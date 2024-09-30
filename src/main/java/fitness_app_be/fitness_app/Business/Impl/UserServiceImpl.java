package fitness_app_be.fitness_app.Business.Impl;

import fitness_app_be.fitness_app.Business.UserService;
import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.ExceptionHandling.UserNotFoundException;
import fitness_app_be.fitness_app.Persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Override
    public List<User> searchUsersByPartialUsername(String partialUsername) {
        return userRepository.findByUsernameContainingIgnoreCase(partialUsername);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + user.getEmail() + " not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setFitnessGoal(user.getFitnessGoal());
        existingUser.setDietPreference(user.getDietPreference());

        return userRepository.save(existingUser);
    }
}
