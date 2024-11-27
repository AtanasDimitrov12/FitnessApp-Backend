package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.business.AuthService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.configuration.security.PasswordEncodeConfig;
import fitness_app_be.fitness_app.configuration.security.token.AccessTokenEncoder;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.configuration.security.token.AccessToken;
import fitness_app_be.fitness_app.configuration.security.token.impl.AccessTokenImpl;
import fitness_app_be.fitness_app.exception_handling.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AdminService adminService;
    private final PasswordEncodeConfig passwordEncodeConfig;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public void register(User user) {
        // Hash the password and set default role
        user.setPassword(passwordEncodeConfig.createBCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);

        // Use UserService to create the user
        userService.createUser(user);
    }

    @Override
    public String authenticateUser(String username, String password) {
        Optional<User> userOptional = userService.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User storedUser = userOptional.get();
        if (!passwordEncodeConfig.createBCryptPasswordEncoder().matches(password, storedUser.getPassword())) {
            throw new InvalidCredentialsException();
        }

        AccessToken accessToken = new AccessTokenImpl(
                storedUser.getUsername(),
                storedUser.getId(),
                Set.of(storedUser.getRole().toString())
        );
        return accessTokenEncoder.encode(accessToken);
    }

    @Override
    public String authenticateAdmin(String email, String password) {
        Optional<Admin> adminOptional = adminService.findAdminByEmail(email);

        if (adminOptional.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        Admin storedAdmin = adminOptional.get();
        if (!passwordEncodeConfig.createBCryptPasswordEncoder().matches(password, storedAdmin.getPassword())) {
            throw new InvalidCredentialsException();
        }

        AccessToken accessToken = new AccessTokenImpl(
                storedAdmin.getEmail(),
                storedAdmin.getId(),
                Set.of(storedAdmin.getRole().toString())
        );
        return accessTokenEncoder.encode(accessToken);
    }

    @Override
    public boolean verifyPassword(String storedPassword, String inputPassword) {

        return passwordEncodeConfig.createBCryptPasswordEncoder().matches(inputPassword, storedPassword);
    }
}

