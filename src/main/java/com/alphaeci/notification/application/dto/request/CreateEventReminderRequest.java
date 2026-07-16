package com.alphaeci.notification.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Reminder to schedule for a saved event")
public class CreateEventReminderRequest {

    @NotNull
    @Schema(description = "Event to be reminded about", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c")
    private UUID eventId;

    @NotNull
    @Future
    @Schema(description = "Event start date and time; must be in the future",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-05-10T14:00:00")
    private LocalDateTime eventDate;
}
