package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.request.SendNotificationRequest;

public interface SendNotificationUseCase {
    void execute(SendNotificationRequest request);
}
