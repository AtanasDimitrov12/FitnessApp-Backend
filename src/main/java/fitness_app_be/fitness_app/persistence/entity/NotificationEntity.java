package fitness_app_be.fitness_app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Ensure message is not null
    private String message;

    @Column(nullable = false) // Ensure isRead is not null
    private Boolean isRead;

    @Column(nullable = false) // Associate with a user ID
    private Long userId;

    @CreationTimestamp // Automatically sets the creation timestamp
    @Column(updatable = false) // Prevent updates to the creation timestamp
    private LocalDateTime createdAt;
}
