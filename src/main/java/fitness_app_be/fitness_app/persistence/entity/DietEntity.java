package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "diet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TrainerEntity trainer;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

    @OneToMany(mappedBy = "diet")
    private List<MealEntity> meals;

    @ManyToMany
    @JoinTable(
            name = "user_diets",
            joinColumns = @JoinColumn(name = "diet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;
}
