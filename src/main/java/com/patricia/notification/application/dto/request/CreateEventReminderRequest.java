package com.patricia.notification.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CreateEventReminderRequest {

    @NotNull
    private UUID eventId;

    @NotNull
    @Future
    private LocalDateTime eventDate;
}
