package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification create(Notification notification);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    Notification update(Notification notification);
}
