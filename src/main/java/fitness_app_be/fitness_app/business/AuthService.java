package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.domain.User;

public interface AuthService {
    void register(User user);
    void adminRegister(Admin admin);
    String authenticateUser(String username, String password);
    String authenticateAdmin(String email, String password);
    boolean verifyPassword(String storedPassword, String inputPassword);
}
