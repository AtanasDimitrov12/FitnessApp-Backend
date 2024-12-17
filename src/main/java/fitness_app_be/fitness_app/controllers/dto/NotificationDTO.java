package fitness_app_be.fitness_app.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private Boolean isRead;
    private Long userId;
    private LocalDateTime createdAt;
}
