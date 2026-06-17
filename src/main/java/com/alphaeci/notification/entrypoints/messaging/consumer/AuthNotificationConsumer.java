package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.AuthNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.auth}")
    public void consume(AuthNotificationEvent event) {
        String title = switch (event.getType()) {
            case OTP_VERIFICATION -> "Código de verificación";
            case PASSWORD_RESET -> "Recuperación de contraseña";
            default -> "Notificación de autenticación";
        };

        String body = switch (event.getType()) {
            case OTP_VERIFICATION -> "Tu código OTP es: " + event.getOtp();
            case PASSWORD_RESET -> "Haz clic para restablecer tu contraseña";
            default -> "Revisa tu cuenta";
        };

        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(event.getType())
                .channel(NotificationChannel.EMAIL)
                .title(title)
                .body(body)
                .referenceId(event.getReferenceId())
                .build());
    }
}
