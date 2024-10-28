package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    Admin getAdminById(Long id);
    Admin createAdmin(Admin trainer);
    void deleteAdmin(Long id);
    Admin getAdminByEmail(String email);
    Admin updateAdmin(Admin trainer);
}
