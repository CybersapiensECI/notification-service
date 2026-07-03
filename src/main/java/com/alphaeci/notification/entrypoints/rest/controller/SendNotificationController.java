package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/notifications", produces = "application/json")
@RequiredArgsConstructor
public class SendNotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody SendNotificationRequest request) {
        sendNotificationUseCase.execute(request);
        return ResponseEntity.status(201).build();
    }
}
