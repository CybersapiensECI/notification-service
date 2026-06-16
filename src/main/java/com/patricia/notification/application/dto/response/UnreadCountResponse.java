package com.patricia.notification.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UnreadCountResponse {

    private UUID userId;
    private long unreadCount;
}
