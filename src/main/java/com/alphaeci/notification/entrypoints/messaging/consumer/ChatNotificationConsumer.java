package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.ChatNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.chat}")
    public void consume(ChatNotificationEvent event) {
        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(NotificationType.PARCHE_MESSAGE)
                .channel(NotificationChannel.IN_APP)
                .title("Nuevo mensaje")
                .body(event.getSenderName() + " te envió un mensaje")
                .referenceId(event.getChatId())
                .build());
    }
}
