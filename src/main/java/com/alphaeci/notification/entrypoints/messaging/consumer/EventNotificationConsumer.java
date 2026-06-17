package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.domain.ports.in.CreateEventReminderUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.EventNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventNotificationConsumer {

    private final CreateEventReminderUseCase createEventReminderUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.event}")
    public void consume(EventNotificationEvent event) {
        createEventReminderUseCase.execute(
                event.getTargetUserId(),
                CreateEventReminderRequest.builder()
                        .eventId(event.getEventId())
                        .eventDate(event.getEventDate())
                        .build()
        );
    }
}
