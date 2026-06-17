package com.alphaeci.notification.domain.ports.out;

import java.util.UUID;

import com.alphaeci.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
    Page<Notification> findAllByUserId(UUID userId, Pageable pageable);
    long countUnreadByUserId(UUID userId);
    void markAsRead(UUID notificationId);
    void markAllAsRead(UUID userId);
}