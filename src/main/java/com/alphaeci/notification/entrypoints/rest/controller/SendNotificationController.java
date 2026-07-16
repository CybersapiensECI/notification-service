package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/notifications", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Notifications — Send", description = "Creation and delivery of notifications")
public class SendNotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;

    @Operation(
            summary = "Send a notification",
            description = """
                    Creates a notification, persists it and delivers it in real time over WebSocket to
                    /user/{userId}/queue/notifications.

                    Preferences are checked first: if the user has disabled the requested type, the
                    notification is discarded and 204 No Content is returned. Critical types
                    (OTP_VERIFICATION, PASSWORD_RESET) bypass the preference check and are additionally
                    delivered by email.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notification created and delivered"),
            @ApiResponse(responseCode = "204", description = "Notification type disabled in the user's preferences; discarded"),
            @ApiResponse(responseCode = "400", description = "Invalid payload (missing fields, title over 80 or body over 200 characters)")
    })
    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody SendNotificationRequest request) {
        sendNotificationUseCase.execute(request);
        return ResponseEntity.status(201).build();
    }
}
