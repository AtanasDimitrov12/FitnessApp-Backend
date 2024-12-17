package fitness_app_be.fitness_app.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String message;
    private Boolean isRead;
    private Long userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
