package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.AdminService;
import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.exception_handling.AdminNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.getAll();
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.getAdminById(id)
                .orElseThrow(() -> new AdminNotFoundException(id));
    }

    @Override
    public Admin createAdmin(Admin trainer) {
        return adminRepository.create(trainer);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.exists(id)) {
            throw new AdminNotFoundException(id);
        }
        adminRepository.delete(id);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Trainer with email: " + email + " not found"));
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email){
        return adminRepository.findByEmail(email);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        Admin existingAdmin = adminRepository.findByEmail(admin.getEmail())
                .orElseThrow(() -> new AdminNotFoundException("Admin with email " + admin.getEmail() + " not found"));


        existingAdmin.setPassword(admin.getPassword());

        return adminRepository.update(existingAdmin);
    }
}
