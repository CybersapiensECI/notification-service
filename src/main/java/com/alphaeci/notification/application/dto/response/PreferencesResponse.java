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
@Schema(description = "Notification types the user has enabled")
public class PreferencesResponse {

    @Schema(description = "Owner of the preferences", example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
    private UUID userId;

    @Schema(description = "Receive CONNECTION_REQUEST notifications", example = "true")
    private boolean connectionRequest;

    @Schema(description = "Receive PARCHE_MESSAGE notifications", example = "true")
    private boolean parcheMessage;

    @Schema(description = "Receive EVENT_REMINDER notifications", example = "true")
    private boolean eventReminder;

    @Schema(description = "Receive NEARBY_PARCHE notifications", example = "false")
    private boolean nearbyParche;

    @Schema(description = "Receive ACHIEVEMENT_UNLOCKED notifications", example = "true")
    private boolean achievementUnlocked;

    @Schema(description = "Receive PARCHE_INVITATION notifications", example = "true")
    private boolean parcheInvitation;

    @Schema(description = "Last update timestamp", example = "2026-04-15T10:30:00")
    private LocalDateTime updatedAt;
}
