package fitness_app_be.fitness_app.controllers.dto.Authentication;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String token;
}

