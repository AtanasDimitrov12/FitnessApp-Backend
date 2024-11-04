package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "exercises")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sets", nullable = false)
    private int sets;

    @Column(name = "reps", nullable = false)
    private int reps;

    @Column(name = "muscleGroup", nullable = false)
    private String muscleGroup;

    @ManyToMany(mappedBy = "exercises")
    private List<WorkoutEntity> workouts;
}
