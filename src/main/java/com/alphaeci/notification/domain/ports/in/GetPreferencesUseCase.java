package com.alphaeci.notification.domain.ports.in;

import com.alphaeci.notification.application.dto.response.PreferencesResponse;

public interface GetPreferencesUseCase {
    PreferencesResponse execute(String userId);
}