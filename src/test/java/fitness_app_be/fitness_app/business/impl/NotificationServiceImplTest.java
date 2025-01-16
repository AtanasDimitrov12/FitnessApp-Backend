package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.persistence.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification(1L, "Test Notification", false, 100L, LocalDateTime.now());
    }

    @Test
    void createNotification_ShouldReturnCreatedNotification() {
        when(notificationRepository.create(any(Notification.class))).thenReturn(notification);

        Notification createdNotification = notificationService.createNotification(100L, "Test Notification");

        assertNotNull(createdNotification);
        assertEquals(100L, createdNotification.getUserId());
        assertEquals("Test Notification", createdNotification.getMessage());
        assertFalse(createdNotification.getIsRead());
        verify(notificationRepository, times(1)).create(any(Notification.class));
    }

    @Test
    void getUnreadNotifications_ShouldReturnListOfUnreadNotifications() {
        when(notificationRepository.findByUserIdAndIsReadFalse(100L)).thenReturn(List.of(notification));

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(100L);

        assertNotNull(unreadNotifications);
        assertEquals(1, unreadNotifications.size());
        assertFalse(unreadNotifications.get(0).getIsRead());
        verify(notificationRepository, times(1)).findByUserIdAndIsReadFalse(100L);
    }

    @Test
    void markAsRead_ShouldUpdateNotificationToRead() {
        when(notificationRepository.findByUserIdAndIsReadFalse(1L)).thenReturn(List.of(notification));
        when(notificationRepository.update(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(1L);

        assertTrue(notification.getIsRead());
        verify(notificationRepository, times(1)).update(notification);
    }

    @Test
    void markAsRead_ShouldThrowException_WhenNotificationNotFound() {
        when(notificationRepository.findByUserIdAndIsReadFalse(1L)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> notificationService.markAsRead(1L));
        assertEquals("Notification not found", exception.getMessage());
    }

    @Test
    void markAllAsRead_ShouldMarkAllNotificationsAsRead() {
        Notification notification2 = new Notification(2L, "Second Notification", false, 100L, LocalDateTime.now());

        List<Notification> unreadNotifications = List.of(notification, notification2);
        when(notificationRepository.findByUserIdAndIsReadFalse(100L)).thenReturn(unreadNotifications);
        when(notificationRepository.update(any(Notification.class))).thenReturn(notification);

        notificationService.markAllAsRead(100L);

        assertTrue(notification.getIsRead());
        assertTrue(notification2.getIsRead());
        verify(notificationRepository, times(2)).update(any(Notification.class));
    }
}
