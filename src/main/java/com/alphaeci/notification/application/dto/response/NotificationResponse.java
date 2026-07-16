package com.alphaeci.notification.application.dto.response;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A notification as stored and delivered to the user")
public class NotificationResponse {

    @Schema(description = "Notification identifier", example = "9e8d7c6b-5a4f-43e2-b1c0-9d8e7f6a5b4c")
    private UUID id;

    @Schema(description = "Owner of the notification", example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
    private UUID userId;

    @Schema(description = "Notification type", example = "PARCHE_INVITATION")
    private NotificationType type;

    @Schema(description = "Delivery channel", example = "IN_APP")
    private NotificationChannel channel;

    @Schema(description = "Visible title", example = "Invitación a parche")
    private String title;

    @Schema(description = "Message body", example = "Te han invitado al parche: Parche de estudio")
    private String body;

    @Schema(description = "Whether the user has read it", example = "false")
    private boolean read;

    @Schema(description = "Resource that originated the notification",
            example = "8c5b2d10-4e6f-41a2-9d7c-6b3f0a1e8d24")
    private UUID referenceId;

    @Schema(description = "Creation timestamp", example = "2026-04-15T10:30:00")
    private LocalDateTime createdAt;
}
