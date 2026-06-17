package com.alphaeci.notification.entrypoints.messaging.dto;

import com.alphaeci.notification.domain.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthNotificationEvent {

    private UUID targetUserId;
    private UUID referenceId;
    private NotificationType type;
    private String otp;
}
