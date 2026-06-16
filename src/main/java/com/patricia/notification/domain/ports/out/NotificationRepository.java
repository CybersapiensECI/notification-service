package com.patricia.notification.domain.ports.out;

import com.patricia.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
    Page<Notification> findAllByUserId(UUID userId, Pageable pageable);
    long countUnreadByUserId(UUID userId);
    void markAsRead(UUID notificationId);
    void markAllAsRead(UUID userId);
}
