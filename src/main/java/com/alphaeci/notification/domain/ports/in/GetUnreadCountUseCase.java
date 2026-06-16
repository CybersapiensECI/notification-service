package com.alphaeci.notification.domain.ports.in;

import java.util.UUID;

import com.alphaeci.notification.application.dto.response.UnreadCountResponse;

public interface GetUnreadCountUseCase {
    UnreadCountResponse execute(UUID userId);
}