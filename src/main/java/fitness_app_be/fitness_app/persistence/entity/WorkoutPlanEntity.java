package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "workout_plan_id")
    private List<WorkoutEntity> workouts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "workout_plan_fitness_goals", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "fitness_goal")
    private List<String> fitnessGoals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "workout_plan_training_styles", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "training_style")
    private List<String> trainingStyle = new ArrayList<>();
}
