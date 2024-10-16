package fitness_app_be.fitness_app.persistence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "workouts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String pictureURL;

    @ElementCollection
    @CollectionTable(name = "workout_exercises", joinColumns = @JoinColumn(name = "workout_id"))
    @Column(name = "exercise")
    private List<String> exercises;
}
