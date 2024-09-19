package fitness_app_be.fitness_app.BusinessLayer;

import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> searchUsersByPartialUsername(String partialUsername);
}
