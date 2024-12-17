package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Long userId, String message);
    List<Notification> getUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
}
