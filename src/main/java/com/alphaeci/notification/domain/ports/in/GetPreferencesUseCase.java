package com.alphaeci.notification.domain.ports.in;

import java.util.UUID;

import com.alphaeci.notification.application.dto.response.PreferencesResponse;

public interface GetPreferencesUseCase {
    PreferencesResponse execute(UUID userId);
}