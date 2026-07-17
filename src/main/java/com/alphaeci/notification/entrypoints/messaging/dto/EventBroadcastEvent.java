package com.alphaeci.notification.entrypoints.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Evento nuevo creado en EventService, para publicitarlo por push (topic
 * FCM "events_broadcast") — no un recordatorio dirigido a un usuario.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventBroadcastEvent {

    private String id;
    private String name;
    private String description;
    private String category;
    private String date;
}
