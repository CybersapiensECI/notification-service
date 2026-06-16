package com.alphaeci.notification.application.dto.request;

import java.util.UUID;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendNotificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationChannel channel;

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String body;

    private UUID referenceId;
}