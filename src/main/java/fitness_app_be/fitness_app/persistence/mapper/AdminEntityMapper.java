package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.persistence.entity.AdminEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class AdminEntityMapper {

    public Admin toDomain(AdminEntity adminEntity) {
        if (adminEntity == null) {
            return null;
        }
        return new Admin(
                adminEntity.getId(),
                adminEntity.getEmail(),
                adminEntity.getPassword(),
                adminEntity.getRole()
        );
    }

    public AdminEntity toEntity(Admin trainer) {
        if (trainer == null) {
            return null;
        }

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setId(trainer.getId());
        adminEntity.setEmail(trainer.getEmail());
        adminEntity.setPassword(trainer.getPassword());
        adminEntity.setRole(trainer.getRole());

        return adminEntity;
    }
}
