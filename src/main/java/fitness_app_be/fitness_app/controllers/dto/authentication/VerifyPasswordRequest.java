package fitness_app_be.fitness_app.controllers.dto.authentication;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPasswordRequest {
    private String username;
    private String password;

}
