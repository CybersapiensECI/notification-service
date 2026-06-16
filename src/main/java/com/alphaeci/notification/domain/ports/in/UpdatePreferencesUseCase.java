package com.alphaeci.notification.domain.ports.in;

import java.util.UUID;

import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;

public interface UpdatePreferencesUseCase {
    PreferencesResponse execute(UUID userId, UpdatePreferencesRequest request);
}