package com.alphaeci.notification.domain.ports.in;

import java.util.UUID;

public interface MarkAsReadUseCase {
    void markOne(UUID userId, UUID notificationId);
    void markAll(UUID userId);
}