package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to WorkoutPlanEntity
    @ManyToOne
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlanEntity workoutPlan;

    // Reference to WorkoutEntity
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    // Field to track completion status
    @Column(name = "is_done", nullable = false)
    private Boolean isDone = false;

    // Optional: Field to track the current week
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;
}
