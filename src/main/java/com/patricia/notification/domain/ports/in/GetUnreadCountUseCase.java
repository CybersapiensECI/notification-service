package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.response.UnreadCountResponse;

import java.util.UUID;

public interface GetUnreadCountUseCase {
    UnreadCountResponse execute(UUID userId);
}
