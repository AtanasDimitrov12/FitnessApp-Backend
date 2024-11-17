package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.AuthService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.configuration.security.token.AccessTokenEncoder;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.configuration.security.token.AccessToken;
import fitness_app_be.fitness_app.configuration.security.token.impl.AccessTokenImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService; // Use UserService here
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public void register(User user) {
        // Hash the password and set default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        // Use UserService to create the user
        userService.createUser(user);
    }

    @Override
    public String authenticateUser(String username, String password) {
        // Use UserService to retrieve the user by username
        Optional<User> userOptional = userService.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            System.out.println("Username not found: " + username);
            throw new RuntimeException("Invalid username or password");
        }

        User storedUser = userOptional.get();
        boolean passwordMatches = passwordEncoder.matches(password, storedUser.getPassword());

        if (!passwordMatches) {
            System.out.println("Password does not match for user: " + username);
            throw new RuntimeException("Invalid username or password");
        }

        AccessToken accessToken = new AccessTokenImpl(
                storedUser.getUsername(),
                storedUser.getId(),
                Set.of(storedUser.getRole().toString())
        );
        return accessTokenEncoder.encode(accessToken);
    }

}
