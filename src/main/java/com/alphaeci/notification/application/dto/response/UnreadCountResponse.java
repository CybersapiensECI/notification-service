package com.alphaeci.notification.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Number of unread notifications for a user")
public class UnreadCountResponse {

    @Schema(description = "User the count belongs to", example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
    private UUID userId;

    @Schema(description = "Unread notifications", example = "5")
    private long unreadCount;
}
