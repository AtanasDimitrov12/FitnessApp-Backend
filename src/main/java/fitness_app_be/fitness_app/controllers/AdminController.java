package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.controllers.dto.AdminDTO;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.controllers.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    // Get a list of all admins - accessible only to ADMIN users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AdminDTO> getAllAdmins() {
        return adminService.getAllAdmins().stream()
                .map(adminMapper::domainToDto)
                .toList();
    }

    // Get details of a specific admin by ID - accessible only to ADMIN users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public AdminDTO getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return adminMapper.domainToDto(admin);
    }

    // Create a new admin - accessible only to ADMIN users
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AdminDTO createAdmin(@RequestBody AdminDTO adminDTO) {
        Admin admin = adminService.createAdmin(adminMapper.toDomain(adminDTO));
        return adminMapper.domainToDto(admin);
    }

    // Update an existing admin - accessible only to ADMIN users
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public AdminDTO updateAdmin(@RequestBody AdminDTO adminDTO) {
        Admin admin = adminMapper.toDomain(adminDTO);
        Admin updatedAdmin = adminService.updateAdmin(admin);
        return adminMapper.domainToDto(updatedAdmin);
    }

    // Delete an admin by ID - accessible only to ADMIN users
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
