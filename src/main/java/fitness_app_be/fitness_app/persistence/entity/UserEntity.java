package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "app_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "fitness_goal")
    private String fitnessGoal;

    @Column(name = "diet_preference")
    private String dietPreference;

    @Column(name = "picture_url")
    private String pictureURL;

    @ManyToMany(mappedBy = "users")
    private List<WorkoutEntity> workouts;

    @ManyToMany(mappedBy = "users")
    private List<DietEntity> diets;

    @OneToMany(mappedBy = "user")
    private List<ProgressNoteEntity> progressNotes;
}


