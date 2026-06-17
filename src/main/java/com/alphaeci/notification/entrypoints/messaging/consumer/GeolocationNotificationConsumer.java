package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.GeolocationNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeolocationNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.geolocation}")
    public void consume(GeolocationNotificationEvent event) {
        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(NotificationType.NEARBY_PARCHE)
                .channel(NotificationChannel.IN_APP)
                .title("Parche cercano")
                .body("Hay un parche cerca: " + event.getParcheName() + " en " + event.getLocation())
                .referenceId(event.getParcheId())
                .build());
    }
}
