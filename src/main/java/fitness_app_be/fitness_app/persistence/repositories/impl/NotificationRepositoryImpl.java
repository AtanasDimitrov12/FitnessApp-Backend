package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.persistence.entity.NotificationEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaNotificationRepository;
import fitness_app_be.fitness_app.persistence.mapper.NotificationEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpaNotificationRepository;
    private final NotificationEntityMapper notificationEntityMapper;

    @Override
    public Notification create(Notification notification) {
        return notificationEntityMapper.toDomain(jpaNotificationRepository.save(notificationEntityMapper.toEntity(notification)));
    }

    @Override
    public List<Notification> findByUserIdAndIsReadFalse(Long userId) {
        // Fetch NotificationEntity list from the JPA repository
        List<NotificationEntity> entities = jpaNotificationRepository.findByUserIdAndIsReadFalse(userId);

        // Initialize an empty list for domain objects
        List<Notification> notifications = new ArrayList<>();

        // Map each NotificationEntity to Notification domain object using a for-each loop
        for (NotificationEntity entity : entities) {
            notifications.add(notificationEntityMapper.toDomain(entity));
        }

        // Return the mapped list
        return notifications;
    }



    @Override
    public Notification update(Notification notification) {
        return notificationEntityMapper.toDomain(jpaNotificationRepository.save(notificationEntityMapper.toEntity(notification)));
    }
}
