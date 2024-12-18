package fitness_app_be.fitness_app.persistence.entity;

import fitness_app_be.fitness_app.domain.FitnessGoal;
import fitness_app_be.fitness_app.domain.FitnessLevel;
import fitness_app_be.fitness_app.domain.TrainingStyle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_workout_preference")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkoutPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "fitness_goal")
    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @Column(name = "fitness_level")
    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    @Column(name = "preferred_training_style")
    @Enumerated(EnumType.STRING)
    private TrainingStyle preferredTrainingStyle;

    @Column(name = "days_available")
    private int daysAvailable;
}
