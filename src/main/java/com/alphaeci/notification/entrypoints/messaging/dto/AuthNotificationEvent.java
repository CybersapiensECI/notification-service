package com.alphaeci.notification.entrypoints.messaging.dto;

import com.alphaeci.notification.domain.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event published by the identity-service on {@code notification.exchange} with routing keys
 * {@code auth.otp} and {@code auth.password-reset}.
 *
 * <p>{@code email} carries the recipient address: this service has no user directory, so the address
 * to deliver the OTP or reset code to must travel with the event.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthNotificationEvent {

    private UUID targetUserId;
    private UUID referenceId;
    private NotificationType type;
    private String email;
    private String otp;
}
