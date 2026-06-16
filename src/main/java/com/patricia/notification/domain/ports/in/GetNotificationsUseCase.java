package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetNotificationsUseCase {
    Page<NotificationResponse> execute(UUID userId, Pageable pageable);
}
