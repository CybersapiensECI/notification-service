package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.domain.ports.in.CreateEventReminderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications/reminders")
@RequiredArgsConstructor
public class EventReminderController {

    private final CreateEventReminderUseCase createEventReminderUseCase;

    @PostMapping
    public ResponseEntity<EventReminderResponse> createReminder(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateEventReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createEventReminderUseCase.execute(userId, request));
    }
}
