package com.alphaeci.notification.domain.ports.in;

public interface MarkAsReadUseCase {
    void markOne(String userId, String notificationId);
    void markAll(String userId);
}