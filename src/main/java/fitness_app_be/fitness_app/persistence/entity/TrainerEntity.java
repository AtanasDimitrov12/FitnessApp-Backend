package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "trainer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "expertise")
    private String expertise;

    @Column(name = "picture_url")
    private String pictureURL;

    @OneToMany(mappedBy = "trainer")
    private List<WorkoutEntity> workoutsCreated;

    @OneToMany(mappedBy = "trainer")
    private List<DietEntity> dietsCreated;
}
