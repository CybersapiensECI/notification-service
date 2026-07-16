package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.domain.ports.in.CreateEventReminderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/notifications/reminders", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Event Reminders", description = "Scheduled reminders for saved events")
public class EventReminderController {

    private final CreateEventReminderUseCase createEventReminderUseCase;

    @Operation(
            summary = "Create an event reminder",
            description = """
                    Registers a reminder for a saved event. A scheduler runs every 10 minutes and sends
                    EVENT_REMINDER notifications 24 hours and 1 hour before the event starts. The
                    reminded24h and reminded1h flags prevent duplicates.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reminder created"),
            @ApiResponse(responseCode = "400", description = "Missing fields or eventDate not in the future")
    })
    @PostMapping
    public ResponseEntity<EventReminderResponse> createReminder(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateEventReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createEventReminderUseCase.execute(userId, request));
    }
}
