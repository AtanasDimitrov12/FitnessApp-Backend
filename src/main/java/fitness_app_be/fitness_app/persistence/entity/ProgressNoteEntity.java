package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "weight")
    private double weight;

    @Column(name = "note")
    private String note;

    @Column(name = "picture_url")
    private String pictureURL;
}