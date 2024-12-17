package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.persistence.entity.NotificationEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class NotificationEntityMapper {

    public Notification toDomain(NotificationEntity notificationEntity) {
        if (notificationEntity == null) {
            return null;
        }

        return Notification.builder()
                .id(notificationEntity.getId())
                .userId(notificationEntity.getUserId())
                .message(notificationEntity.getMessage())
                .isRead(notificationEntity.getIsRead())
                .createdAt(notificationEntity.getCreatedAt())
                .build();
    }

    public NotificationEntity toEntity(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(notification.getId());
        notificationEntity.setUserId(notification.getUserId());
        notificationEntity.setMessage(notification.getMessage());
        notificationEntity.setIsRead(notification.getIsRead());
        notificationEntity.setCreatedAt(notification.getCreatedAt());

        return notificationEntity;
    }
}
