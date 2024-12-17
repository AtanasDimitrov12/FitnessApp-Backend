package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.NotificationDTO;
import fitness_app_be.fitness_app.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toDomain(NotificationDTO notificationDTO) {
        if (notificationDTO == null) {
            return null;
        }

        return new Notification(
                notificationDTO.getId(),
                notificationDTO.getMessage(),
                notificationDTO.getIsRead(),
                notificationDTO.getUserId(),
                notificationDTO.getCreatedAt()
        );
    }

    public NotificationDTO domainToDto(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getUserId(),
                notification.getCreatedAt()
        );
    }
}
