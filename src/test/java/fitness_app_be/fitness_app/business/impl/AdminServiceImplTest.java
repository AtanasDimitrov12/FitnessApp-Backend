package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.exception_handling.AdminNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin(1L, "admin@example.com", "password");
    }

    @Test
    void getAllAdmins_ShouldReturnListOfAdmins() {
        when(adminRepository.getAll()).thenReturn(List.of(admin));

        List<Admin> admins = adminService.getAllAdmins();

        assertNotNull(admins);
        assertEquals(1, admins.size());
        assertEquals(admin, admins.get(0));
        verify(adminRepository, times(1)).getAll();
    }

    @Test
    void getAdminById_ShouldReturnAdmin_WhenAdminExists() {
        when(adminRepository.getAdminById(1L)).thenReturn(Optional.of(admin));

        Admin foundAdmin = adminService.getAdminById(1L);

        assertNotNull(foundAdmin);
        assertEquals(admin, foundAdmin);
        verify(adminRepository, times(1)).getAdminById(1L);
    }

    @Test
    void getAdminById_ShouldThrowException_WhenAdminNotFound() {
        when(adminRepository.getAdminById(1L)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminById(1L));
        verify(adminRepository, times(1)).getAdminById(1L);
    }

    @Test
    void createAdmin_ShouldReturnCreatedAdmin() {
        when(adminRepository.create(any(Admin.class))).thenReturn(admin);

        Admin createdAdmin = adminService.createAdmin(admin);

        assertNotNull(createdAdmin);
        assertEquals(admin, createdAdmin);
        verify(adminRepository, times(1)).create(admin);
    }

    @Test
    void deleteAdmin_ShouldDeleteAdmin_WhenAdminExists() {
        when(adminRepository.exists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> adminService.deleteAdmin(1L));
        verify(adminRepository, times(1)).exists(1L);
        verify(adminRepository, times(1)).delete(1L);
    }

    @Test
    void deleteAdmin_ShouldThrowException_WhenAdminNotFound() {
        when(adminRepository.exists(1L)).thenReturn(false);

        assertThrows(AdminNotFoundException.class, () -> adminService.deleteAdmin(1L));
        verify(adminRepository, times(1)).exists(1L);
        verify(adminRepository, never()).delete(1L);
    }

    @Test
    void getAdminByEmail_ShouldReturnAdmin_WhenAdminExists() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        Admin foundAdmin = adminService.getAdminByEmail("admin@example.com");

        assertNotNull(foundAdmin);
        assertEquals(admin, foundAdmin);
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void getAdminByEmail_ShouldThrowException_WhenAdminNotFound() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminByEmail("admin@example.com"));
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void updateAdmin_ShouldReturnUpdatedAdmin_WhenAdminExists() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setEmail("admin@example.com");
        updatedAdmin.setPassword("newPassword");

        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(adminRepository.update(admin)).thenReturn(updatedAdmin);

        Admin result = adminService.updateAdmin(updatedAdmin);

        assertNotNull(result);
        assertEquals("newPassword", result.getPassword());
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
        verify(adminRepository, times(1)).update(admin);
    }

    @Test
    void updateAdmin_ShouldThrowException_WhenAdminNotFound() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setEmail("admin@example.com");
        updatedAdmin.setPassword("newPassword");

        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> adminService.updateAdmin(updatedAdmin));
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
        verify(adminRepository, never()).update(any(Admin.class));
    }
}
