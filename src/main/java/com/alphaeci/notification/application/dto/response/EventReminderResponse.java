package com.alphaeci.notification.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class EventReminderResponse {

    private UUID id;
    private UUID userId;
    private UUID eventId;
    private LocalDateTime eventDate;
    private boolean reminded24h;
    private boolean reminded1h;
}