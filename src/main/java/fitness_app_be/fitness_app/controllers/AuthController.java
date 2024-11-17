package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.AuthService;
import fitness_app_be.fitness_app.controllers.dto.Authentication.JwtResponse;
import fitness_app_be.fitness_app.controllers.dto.Authentication.LoginRequest;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        // Convert UserDTO to User domain model
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        // Register user using the AuthService
        authService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate user and get JWT token
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(new JwtResponse("Invalid username or password"));
        }

        User user = userOptional.get();

        // Authenticate and get JWT token
        String jwtToken = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
}
