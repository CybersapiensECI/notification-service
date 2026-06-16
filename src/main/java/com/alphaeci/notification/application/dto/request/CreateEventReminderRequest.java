package com.alphaeci.notification.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateEventReminderRequest {

    @NotBlank
    private String eventId;

    @NotNull
    @Future
    private LocalDateTime eventDate;
}