package fitness_app_be.fitness_app.controllers;


import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.controllers.dto.AdminDTO;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.controllers.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public List<AdminDTO> getAllAdmins() {

        return adminService.getAllAdmins().stream()
                .map(adminMapper::domainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdminDTO getAdminById(@PathVariable Long id) {

        Admin admin = adminService.getAdminById(id);
        return adminMapper.domainToDto(admin);
    }

    @PostMapping
    public AdminDTO createAdmin(@RequestBody AdminDTO adminDTO) {

        Admin admin = adminService.createAdmin(adminMapper.toDomain(adminDTO));
        return adminMapper.domainToDto(admin);
    }

    @PutMapping
    public AdminDTO updateAdmin(@RequestBody AdminDTO adminDTO) {

        Admin admin = adminMapper.toDomain(adminDTO);
        Admin updatedAdmin = adminService.updateAdmin(admin);
        return adminMapper.domainToDto(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
