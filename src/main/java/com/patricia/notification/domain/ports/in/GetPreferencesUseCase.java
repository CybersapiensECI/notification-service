package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.response.PreferencesResponse;

import java.util.UUID;

public interface GetPreferencesUseCase {
    PreferencesResponse execute(UUID userId);
}
