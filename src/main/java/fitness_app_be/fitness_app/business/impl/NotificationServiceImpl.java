package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.NotificationService;
import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.persistence.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification createNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setIsRead(false);
        return notificationRepository.create(notification);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository
                .findByUserIdAndIsReadFalse(notificationId)
                .stream()
                .findFirst() // Get the first element (if it exists)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.update(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        for(Notification notification : unreadNotifications) {
            notificationRepository.update(notification);
        }
    }


}
