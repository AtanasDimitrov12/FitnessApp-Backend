package fitness_app_be.fitness_app.persistence.entity;

import fitness_app_be.fitness_app.domain.MuscleGroup;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false)
    private MuscleGroup muscleGroup;


    @ManyToMany(mappedBy = "exercises")
    private List<WorkoutEntity> workouts;
}
