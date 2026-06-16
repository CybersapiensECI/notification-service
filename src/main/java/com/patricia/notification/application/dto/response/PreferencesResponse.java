package com.patricia.notification.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PreferencesResponse {

    private UUID userId;
    private boolean connectionRequest;
    private boolean parcheMessage;
    private boolean eventReminder;
    private boolean nearbyParche;
    private boolean achievementUnlocked;
    private boolean parcheInvitation;
    private LocalDateTime updatedAt;
}
