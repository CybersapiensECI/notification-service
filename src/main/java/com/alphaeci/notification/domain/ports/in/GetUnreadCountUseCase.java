package com.alphaeci.notification.domain.ports.in;

import com.alphaeci.notification.application.dto.response.UnreadCountResponse;

public interface GetUnreadCountUseCase {
    UnreadCountResponse execute(String userId);
}