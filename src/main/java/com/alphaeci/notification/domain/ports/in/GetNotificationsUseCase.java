package com.alphaeci.notification.domain.ports.in;

import com.alphaeci.notification.application.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetNotificationsUseCase {
    Page<NotificationResponse> execute(String userId, Pageable pageable);
}