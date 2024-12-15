package fitness_app_be.fitness_app.persistence.entity;

import fitness_app_be.fitness_app.domain.FitnessGoal;
import fitness_app_be.fitness_app.domain.FitnessLevel;
import fitness_app_be.fitness_app.domain.TrainingStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

    // Many-to-Many relationship with ExerciseEntity
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "workout_exercise",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<ExerciseEntity> exercises;

    // Many-to-Many relationship with WorkoutPlanEntity
    @ManyToMany(mappedBy = "workouts")
    private List<WorkoutPlanEntity> workoutPlans;

    // Fitness Goals as Enum stored in a separate table
    @ElementCollection(targetClass = FitnessGoal.class)
    @CollectionTable(name = "workout_fitness_goals", joinColumns = @JoinColumn(name = "workout_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_goal")
    private List<FitnessGoal> fitnessGoals;

    // Fitness Levels as Enum stored in a separate table
    @ElementCollection(targetClass = FitnessLevel.class)
    @CollectionTable(name = "workout_fitness_levels", joinColumns = @JoinColumn(name = "workout_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_level")
    private List<FitnessLevel> fitnessLevels;

    // Training Styles as Enum stored in a separate table
    @ElementCollection(targetClass = TrainingStyle.class)
    @CollectionTable(name = "workout_training_styles", joinColumns = @JoinColumn(name = "workout_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "training_style")
    private List<TrainingStyle> trainingStyles;
}
