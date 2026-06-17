package com.alphaeci.notification.entrypoints.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingNotificationEvent {

    private UUID targetUserId;
    private UUID requesterId;
    private String requesterName;
}
