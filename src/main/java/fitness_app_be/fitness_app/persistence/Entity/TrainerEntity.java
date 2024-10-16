package fitness_app_be.fitness_app.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "trainers")
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String expertise;

    private String pictureURL;
}
