package fitness_app_be.fitness_app.persistence.entity;

import fitness_app_be.fitness_app.domain.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "admins")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING) // Store as string in the database
    private Role role;


}
