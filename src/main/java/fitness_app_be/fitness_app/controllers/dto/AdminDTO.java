package fitness_app_be.fitness_app.controllers.dto;

import fitness_app_be.fitness_app.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
