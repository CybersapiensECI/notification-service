package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.domain.ports.in.GetPreferencesUseCase;
import com.alphaeci.notification.domain.ports.in.UpdatePreferencesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/notifications/preferences", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "Per-user opt-in and opt-out of notification types")
public class PreferencesController {

    private final GetPreferencesUseCase getPreferencesUseCase;
    private final UpdatePreferencesUseCase updatePreferencesUseCase;

    @Operation(
            summary = "Get the user's notification preferences",
            description = "Returns which notification types the authenticated user has enabled.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current preferences"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed X-User-Id header")
    })
    @GetMapping
    public ResponseEntity<PreferencesResponse> getPreferences(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(getPreferencesUseCase.execute(userId));
    }

    @Operation(
            summary = "Update the user's notification preferences",
            description = """
                    Replaces the full set of preference flags for the authenticated user. Disabled types
                    are skipped when a notification is sent, except for the critical types
                    (OTP_VERIFICATION, PASSWORD_RESET), which are always delivered.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferences updated"),
            @ApiResponse(responseCode = "400", description = "Invalid payload or missing X-User-Id header")
    })
    @PutMapping
    public ResponseEntity<PreferencesResponse> updatePreferences(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UpdatePreferencesRequest request) {
        return ResponseEntity.ok(updatePreferencesUseCase.execute(userId, request));
    }
}
