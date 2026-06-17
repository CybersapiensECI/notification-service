package com.alphaeci.notification.entrypoints.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventNotificationEvent {

    private UUID targetUserId;
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventDate;
}
