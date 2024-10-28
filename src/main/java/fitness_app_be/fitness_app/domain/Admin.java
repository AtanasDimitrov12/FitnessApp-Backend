package fitness_app_be.fitness_app.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor()
public class Admin {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String email;
    private String password;
}
