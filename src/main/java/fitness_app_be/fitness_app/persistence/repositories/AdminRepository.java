package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Admin;

import java.util.Optional;
import java.util.List;

public interface AdminRepository {

    boolean exists(long id);

    List<Admin> getAll();

    Admin create(Admin trainer);

    Admin update(Admin trainer);

    void delete(long trainerId);

    Optional<Admin> getAdminById(long adminId);

    Optional<Admin> findByEmail(String email);

}
