package fitness_app_be.fitness_app.ControllerLayer;

import fitness_app_be.fitness_app.BusinessLayer.UserService;
import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // Lombok will generate constructor with final fields
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
