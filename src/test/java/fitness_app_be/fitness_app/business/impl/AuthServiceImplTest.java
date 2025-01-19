package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.business.UserService;
import fitness_app_be.fitness_app.configuration.security.token.AccessTokenEncoder;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.domain.Role;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.exception_handling.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private AdminService adminService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private Admin admin;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testUser", "user@example.com", "password", null, null, "pictureURL", LocalDateTime.now(), LocalDateTime.now(),  Role.ADMIN,null, null, null, true);

        admin = new Admin(1L, "admin@example.com", "password", Role.ADMIN);
    }

    @Test
    void register_ShouldEncodePasswordAndSetRole() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        authService.register(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void adminRegister_ShouldEncodePasswordAndSetRole() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        authService.adminRegister(admin);

        assertEquals("encodedPassword", admin.getPassword());
        assertEquals(Role.ADMIN, admin.getRole());
        verify(adminService, times(1)).createAdmin(admin);
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenCredentialsAreValid() {
        when(userService.findUserByUsername("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("mockedToken");

        String token = authService.authenticateUser("user@example.com", "password");

        assertNotNull(token);
        assertEquals("mockedToken", token);
        verify(userService, times(1)).findUserByUsername("user@example.com");
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenUserNotFound() {
        when(userService.findUserByUsername("user@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser("user@example.com", "password"));
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(userService.findUserByUsername("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser("user@example.com", "wrongPassword"));
    }

    @Test
    void authenticateAdmin_ShouldReturnToken_WhenCredentialsAreValid() {
        when(adminService.findAdminByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("password", admin.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("mockedAdminToken");

        String token = authService.authenticateAdmin("admin@example.com", "password");

        assertNotNull(token);
        assertEquals("mockedAdminToken", token);
        verify(adminService, times(1)).findAdminByEmail("admin@example.com");
    }

    @Test
    void authenticateAdmin_ShouldThrowException_WhenAdminNotFound() {
        when(adminService.findAdminByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateAdmin("admin@example.com", "password"));
    }

    @Test
    void authenticateAdmin_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(adminService.findAdminByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrongPassword", admin.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateAdmin("admin@example.com", "wrongPassword"));
    }

    @Test
    void verifyPassword_ShouldReturnTrue_WhenPasswordsMatch() {
        when(passwordEncoder.matches("inputPassword", "storedPassword")).thenReturn(true);

        assertTrue(authService.verifyPassword("storedPassword", "inputPassword"));
    }

    @Test
    void verifyPassword_ShouldReturnFalse_WhenPasswordsDoNotMatch() {
        when(passwordEncoder.matches("inputPassword", "storedPassword")).thenReturn(false);

        assertFalse(authService.verifyPassword("storedPassword", "inputPassword"));
    }
}