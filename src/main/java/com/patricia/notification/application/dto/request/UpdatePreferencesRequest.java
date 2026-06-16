package com.patricia.notification.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePreferencesRequest {

    private boolean connectionRequest;
    private boolean parcheMessage;
    private boolean eventReminder;
    private boolean nearbyParche;
    private boolean achievementUnlocked;
    private boolean parcheInvitation;
}
