package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.MemberNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.member}")
    public void consume(MemberNotificationEvent event) {
        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(NotificationType.MEMBER_LEFT)
                .channel(NotificationChannel.IN_APP)
                .title("Miembro salió del parche")
                .body(event.getMemberName() + " salió del parche: " + event.getParcheName())
                .referenceId(event.getParcheId())
                .build());
    }
}
