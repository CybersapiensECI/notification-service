package com.alphaeci.notification.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "A scheduled reminder for a saved event")
public class EventReminderResponse {

    @Schema(description = "Reminder identifier", example = "9e8d7c6b-5a4f-43e2-b1c0-9d8e7f6a5b4c")
    private UUID id;

    @Schema(description = "User who saved the event", example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
    private UUID userId;

    @Schema(description = "Event being reminded about", example = "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c")
    private UUID eventId;

    @Schema(description = "Event start date and time", example = "2026-05-10T14:00:00")
    private LocalDateTime eventDate;

    @Schema(description = "Whether the 24-hour reminder was already sent", example = "false")
    private boolean reminded24h;

    @Schema(description = "Whether the 1-hour reminder was already sent", example = "false")
    private boolean reminded1h;
}
