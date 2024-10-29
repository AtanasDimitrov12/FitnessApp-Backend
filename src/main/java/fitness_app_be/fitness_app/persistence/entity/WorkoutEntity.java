package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "workout_exercise",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<ExerciseEntity> exercises = new ArrayList<>();

    @ManyToMany(mappedBy = "workouts")
    private List<WorkoutPlanEntity> workoutPlans = new ArrayList<>();
}
