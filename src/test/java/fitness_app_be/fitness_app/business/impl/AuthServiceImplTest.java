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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private User testUser;
    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword");
        testUser.setRole(Role.USER);

        testAdmin = new Admin();
        testAdmin.setEmail("admin@example.com");
        testAdmin.setPassword("hashedPassword");
        testAdmin.setRole(Role.ADMIN);
    }

    @Test
    void register_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Act
        testUser.setPassword("password");
        authService.register(testUser);

        // Assert
        assertEquals("hashedPassword", testUser.getPassword());
        assertEquals(Role.USER, testUser.getRole());
        verify(userService, times(1)).createUser(testUser);
    }

    @Test
    void adminRegister_ShouldEncodePasswordAndSaveAdmin() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Act
        testAdmin.setPassword("password");
        authService.adminRegister(testAdmin);

        // Assert
        assertEquals("hashedPassword", testAdmin.getPassword());
        assertEquals(Role.ADMIN, testAdmin.getRole());
        verify(adminService, times(1)).createAdmin(testAdmin);
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenCredentialsAreCorrect() {
        // Arrange
        when(userService.findUserByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("mockedToken");

        // Act
        String token = authService.authenticateUser("testUser", "password");

        // Assert
        assertEquals("mockedToken", token);
        verify(userService, times(1)).findUserByUsername("testUser");
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userService.findUserByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser("unknownUser", "password"));
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Arrange
        when(userService.findUserByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser("testUser", "wrongPassword"));
    }

    @Test
    void authenticateAdmin_ShouldReturnToken_WhenCredentialsAreCorrect() {
        // Arrange
        when(adminService.findAdminByEmail("admin@example.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("mockedToken");

        // Act
        String token = authService.authenticateAdmin("admin@example.com", "password");

        // Assert
        assertEquals("mockedToken", token);
        verify(adminService, times(1)).findAdminByEmail("admin@example.com");
    }

    @Test
    void authenticateAdmin_ShouldThrowException_WhenAdminNotFound() {
        // Arrange
        when(adminService.findAdminByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateAdmin("unknown@example.com", "password"));
    }

    @Test
    void authenticateAdmin_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Arrange
        when(adminService.findAdminByEmail("admin@example.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateAdmin("admin@example.com", "wrongPassword"));
    }

    @Test
    void verifyPassword_ShouldReturnTrue_WhenPasswordMatches() {
        // Arrange
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        // Act
        boolean result = authService.verifyPassword("hashedPassword", "password");

        // Assert
        assertTrue(result);
    }

    @Test
    void verifyPassword_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
        // Arrange
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Act
        boolean result = authService.verifyPassword("hashedPassword", "wrongPassword");

        // Assert
        assertFalse(result);
    }
}
