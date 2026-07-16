package com.alphaeci.notification.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Full set of notification preference flags. Every flag must be sent; omitted flags default to false.")
public class UpdatePreferencesRequest {

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
}
