package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private String fitnessGoal;

    @Column(name = "fitness_level")
    private String fitnessLevel;

    @Column(name = "preferred_training_style")
    private String preferredTrainingStyle;

    @Column(name = "days_available")
    private int daysAvailable;
}
