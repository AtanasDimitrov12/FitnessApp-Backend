package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.User;

public interface AuthService {
    void register(User user);
    String authenticateUser(String Username, String Password);
    String authenticateAdmin(String email, String password);
}
