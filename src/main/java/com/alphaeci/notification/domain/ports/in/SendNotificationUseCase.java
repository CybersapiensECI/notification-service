package com.alphaeci.notification.domain.ports.in;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;

public interface SendNotificationUseCase {
    void execute(SendNotificationRequest request);
}