package com.alphaeci.notification.domain.ports.out;

import com.alphaeci.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(String id);
    Page<Notification> findAllByUserId(String userId, Pageable pageable);
    long countUnreadByUserId(String userId);
    void markAsRead(String notificationId);
    void markAllAsRead(String userId);
}