package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "progress_note")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;


    @Column(name = "weight")
    private double weight;

    @Column(name = "note")
    private String note;

    @Column(name = "date")
    private LocalDate date;
}
