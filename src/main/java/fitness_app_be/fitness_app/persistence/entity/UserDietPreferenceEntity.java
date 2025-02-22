package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_diet_preference")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDietPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "calories")
    private int calories;

    @Column(name = "meal_frequency")
    private int mealFrequency;
}
