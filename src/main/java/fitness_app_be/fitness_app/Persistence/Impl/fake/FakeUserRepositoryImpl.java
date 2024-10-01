package fitness_app_be.fitness_app.Persistence.Impl.fake;

import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.Persistence.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {

    private static long nextId = 1;
    private final List<User> savedUsers;

    public FakeUserRepositoryImpl() {
        this.savedUsers = new ArrayList<>();
    }

    @Override
    public boolean exists(long id) {
        return savedUsers.stream().anyMatch(user -> user.getId() == id);
    }

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(savedUsers); // Return unmodifiable list
    }

    @Override
    public User create(User user) {
        user.setId(nextId++);
        savedUsers.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> existingUser = getUserById(user.getId());
        if (existingUser.isPresent()) {
            User foundUser = existingUser.get();
            foundUser.setUsername(user.getUsername());
            foundUser.setEmail(user.getEmail());
            foundUser.setFitnessGoal(user.getFitnessGoal());
            foundUser.setDietPreference(user.getDietPreference());
            return foundUser;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void delete(long userId) {
        Optional<User> existingUser = getUserById(userId);
        existingUser.ifPresent(savedUsers::remove);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return savedUsers.stream().filter(user -> user.getId() == userId).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return savedUsers.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return savedUsers.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    @Override
    public List<User> findByFitnessGoal(String fitnessGoal) {
        List<User> usersWithGoal = new ArrayList<>();
        for (User user : savedUsers) {
            if (user.getFitnessGoal().equalsIgnoreCase(fitnessGoal)) {
                usersWithGoal.add(user);
            }
        }
        return usersWithGoal;
    }

    @Override
    public List<User> findByUsernameContainingIgnoreCase(String partialUsername) {
        List<User> matchingUsers = new ArrayList<>();
        for (User user : savedUsers) {
            if (user.getUsername().toLowerCase().contains(partialUsername.toLowerCase())) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }

    @Override
    public long countByEmail(String email) {
        return savedUsers.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).count();
    }
}
