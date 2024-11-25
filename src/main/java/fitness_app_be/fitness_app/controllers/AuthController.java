package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.AuthService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.controllers.dto.Authentication.JwtResponse;
import fitness_app_be.fitness_app.controllers.dto.Authentication.LoginRequest;
import fitness_app_be.fitness_app.controllers.dto.Authentication.VerifyPasswordRequest;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.controllers.dto.UserDTO;
import fitness_app_be.fitness_app.exception_handling.AdminNotFoundException;
import fitness_app_be.fitness_app.exception_handling.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AuthController(AuthService authService, UserService userService, AdminService adminService) {
        this.authService = authService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        authService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        // User Login Attempt
        try {
            Optional<User> userOptional = userService.findUserByUsername(loginRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String jwtToken = authService.authenticateUser(user.getUsername(), loginRequest.getPassword());
                return ResponseEntity.ok(new JwtResponse(jwtToken));
            }
        } catch (UserNotFoundException e) {
            // Log or handle user-specific issues
            System.out.println("User not found: " + loginRequest.getUsername());
        }

        // Admin Login Attempt
        try {
            Optional<Admin> adminOptional = adminService.findAdminByEmail(loginRequest.getUsername());
            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();
                String jwtToken = authService.authenticateAdmin(admin.getEmail(), loginRequest.getPassword());
                return ResponseEntity.ok(new JwtResponse(jwtToken));
            }
        } catch (AdminNotFoundException e) {
            // Log or handle admin-specific issues
            System.out.println("Admin not found: " + loginRequest.getUsername());
        }

        // Generic failure response for unauthorized access
        return ResponseEntity.status(401).body(new JwtResponse("Invalid username or password"));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyPasswordRequest verifyPasswordRequest) {
        try {
            // Check user password
            Optional<User> userOptional = userService.findUserByUsername(verifyPasswordRequest.getUsername());
            if (userOptional.isPresent()) {
                boolean isPasswordValid = authService.verifyPassword(
                        userOptional.get().getPassword(),
                        verifyPasswordRequest.getPassword()
                );
                return ResponseEntity.ok(isPasswordValid);
            }

            // Check admin password
            Optional<Admin> adminOptional = adminService.findAdminByEmail(verifyPasswordRequest.getUsername());
            if (adminOptional.isPresent()) {
                boolean isPasswordValid = authService.verifyPassword(
                        adminOptional.get().getPassword(),
                        verifyPasswordRequest.getPassword()
                );
                return ResponseEntity.ok(isPasswordValid);
            }
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
        }

        return ResponseEntity.status(401).body(false); // Unauthorized if verification fails
    }
}
