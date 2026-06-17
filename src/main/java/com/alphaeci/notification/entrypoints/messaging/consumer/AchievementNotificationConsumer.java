package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.AchievementNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementNotificationConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.achievement}")
    public void consume(AchievementNotificationEvent event) {
        sendNotificationUseCase.execute(SendNotificationRequest.builder()
                .userId(event.getTargetUserId())
                .type(NotificationType.ACHIEVEMENT_UNLOCKED)
                .channel(NotificationChannel.IN_APP)
                .title("Logro desbloqueado")
                .body("Desbloqueaste el logro: " + event.getAchievementName())
                .referenceId(event.getAchievementId())
                .build());
    }
}
