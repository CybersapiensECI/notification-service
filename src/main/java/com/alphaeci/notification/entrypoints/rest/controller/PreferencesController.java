package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.domain.ports.in.GetPreferencesUseCase;
import com.alphaeci.notification.domain.ports.in.UpdatePreferencesUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications/preferences")
@RequiredArgsConstructor
public class PreferencesController {

    private final GetPreferencesUseCase getPreferencesUseCase;
    private final UpdatePreferencesUseCase updatePreferencesUseCase;

    @GetMapping
    public ResponseEntity<PreferencesResponse> getPreferences(
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(getPreferencesUseCase.execute(userId));
    }

    @PutMapping
    public ResponseEntity<PreferencesResponse> updatePreferences(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UpdatePreferencesRequest request) {
        return ResponseEntity.ok(updatePreferencesUseCase.execute(userId, request));
    }
}
