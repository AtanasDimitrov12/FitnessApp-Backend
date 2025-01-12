package fitness_app_be.fitness_app.ControllerLayer;

import fitness_app_be.fitness_app.BusinessLayer.UserService;
import fitness_app_be.fitness_app.ControllerLayer.DTOs.UserDTO;
import fitness_app_be.fitness_app.Domain.User;
import fitness_app_be.fitness_app.MapperLayer.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDTO> getAllUsers() {

        return userService.getAllUsers().stream()
                .map(userMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);
        return userMapper.domainToDto(user);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {

        User createdUser = userService.createUser(userMapper.toDomain(userDTO));
        return userMapper.domainToDto(createdUser);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {

        User user = userMapper.toDomain(userDTO);
        User updatedUser = userService.updateUser(user);
        return userMapper.domainToDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
