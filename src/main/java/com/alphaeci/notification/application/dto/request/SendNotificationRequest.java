package com.alphaeci.notification.application.dto.request;

import java.util.UUID;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Notification to create and deliver")
public class SendNotificationRequest {

    @NotNull
    @Schema(description = "Target user", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
    private UUID userId;

    @NotNull
    @Schema(description = "Notification type", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "PARCHE_INVITATION")
    private NotificationType type;

    @NotNull
    @Schema(description = "Delivery channel", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "IN_APP")
    private NotificationChannel channel;

    @NotBlank
    @Size(max = 80)
    @Schema(description = "Visible title, at most 80 characters", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 80, example = "Invitación a parche")
    private String title;

    @NotBlank
    @Size(max = 200)
    @Schema(description = "Message body, at most 200 characters", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 200, example = "Te han invitado al parche: Parche de estudio")
    private String body;

    @Schema(description = "Resource that originated the notification",
            example = "8c5b2d10-4e6f-41a2-9d7c-6b3f0a1e8d24")
    private UUID referenceId;

    @Email
    @Schema(description = """
            Address to deliver the email to. Required for the critical types (OTP_VERIFICATION,
            PASSWORD_RESET), which are always emailed; ignored for every other type. This service does
            not know the user's address, so the producer must supply it.
            """, example = "estudiante@mail.escuelaing.edu.co")
    private String recipientEmail;
}
