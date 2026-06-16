package com.alphaeci.notification.application.dto.response;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class NotificationResponse {

    private UUID id;
    private UUID userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String title;
    private String body;
    private boolean read;
    private UUID referenceId;
    private LocalDateTime createdAt;
}