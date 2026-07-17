package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.domain.ports.out.FcmPushPort;
import com.alphaeci.notification.entrypoints.messaging.dto.EventBroadcastEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Evento nuevo creado -> push a todos los dispositivos (topic FCM
 * "events_broadcast"). Publicidad, no un mensaje dirigido a un usuario:
 * por eso no pasa por NotificationRepository/IN_APP, solo push directo.
 */
@Component
@RequiredArgsConstructor
public class EventBroadcastConsumer {

    private static final String TOPIC = "events_broadcast";

    private final FcmPushPort fcmPushPort;

    @RabbitListener(queues = "${rabbitmq.queues.eventBroadcast}")
    public void consume(EventBroadcastEvent event) {
        String title = "Nuevo evento: " + event.getName();
        String body = event.getDescription() != null && !event.getDescription().isBlank()
                ? event.getDescription()
                : "Toca para ver los detalles.";
        fcmPushPort.sendToTopic(TOPIC, title, body, Map.of(
                "type", "EVENT_CREATED",
                "eventId", event.getId() == null ? "" : event.getId()
        ));
    }
}
