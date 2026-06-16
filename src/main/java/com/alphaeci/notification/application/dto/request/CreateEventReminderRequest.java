package com.alphaeci.notification.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventReminderRequest {

    @NotNull
    private UUID eventId;

    @NotNull
    @Future
    private LocalDateTime eventDate;
}
