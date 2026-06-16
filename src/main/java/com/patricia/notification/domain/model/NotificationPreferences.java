package com.patricia.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferences {

    private UUID id;
    private UUID userId;
    private boolean connectionRequest;
    private boolean parcheMessage;
    private boolean eventReminder;
    private boolean nearbyParche;
    private boolean achievementUnlocked;
    private boolean parcheInvitation;
    private LocalDateTime updatedAt;
}
