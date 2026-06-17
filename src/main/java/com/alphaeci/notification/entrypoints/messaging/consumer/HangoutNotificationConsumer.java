package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.HangoutNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HangoutNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.hangout}")
    public void consume(HangoutNotificationEvent event) {
        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(NotificationType.PARCHE_INVITATION)
                .channel(NotificationChannel.IN_APP)
                .title("Invitación a parche")
                .body("Te han invitado al parche: " + event.getParcheName())
                .referenceId(event.getParcheId())
                .build());
    }
}
