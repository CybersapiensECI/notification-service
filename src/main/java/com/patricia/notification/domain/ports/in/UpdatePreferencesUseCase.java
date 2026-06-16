package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.request.UpdatePreferencesRequest;
import com.patricia.notification.application.dto.response.PreferencesResponse;

import java.util.UUID;

public interface UpdatePreferencesUseCase {
    PreferencesResponse execute(UUID userId, UpdatePreferencesRequest request);
}
