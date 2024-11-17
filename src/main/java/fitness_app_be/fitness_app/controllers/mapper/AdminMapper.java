package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.AdminDTO;
import fitness_app_be.fitness_app.domain.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toDomain(AdminDTO adminDTO) {
        return new Admin(adminDTO.getId(), adminDTO.getEmail(), adminDTO.getPassword(), adminDTO.getRole());
    }

    public AdminDTO domainToDto(Admin admin) {
        return new AdminDTO(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getRole());
    }
}
