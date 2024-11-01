package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "workout_plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "workoutPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserEntity> users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "plan_workout",
            joinColumns = @JoinColumn(name = "workout_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<WorkoutEntity> workouts;

    @ElementCollection
    @CollectionTable(name = "workout_plan_fitness_goals", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "fitness_goal")
    private List<String> fitnessGoals;

    @ElementCollection
    @CollectionTable(name = "workout_plan_training_styles", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "training_style")
    private List<String> trainingStyle;
}
