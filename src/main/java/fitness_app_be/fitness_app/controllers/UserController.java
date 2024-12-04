package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.controllers.mapper.UserMapper;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.CustomFileUploadException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserProfileUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::domainToDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.domainToDto(user);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(userMapper.toDomain(userDTO));
        return userMapper.domainToDto(createdUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        User updatedUser = userService.updateUser(user);
        return userMapper.domainToDto(updatedUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{id}/upload-profile-picture")
    public UserDTO uploadProfilePicture(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image) {

        try {
            // Pass the MultipartFile directly to the service method
            User updatedUser = userService.uploadUserProfilePicture(id, image);
            return userMapper.domainToDto(updatedUser);
        } catch (UserNotFoundException e) {
            // Return 404 Not Found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CustomFileUploadException | UserProfileUpdateException e) {
            // Return 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
