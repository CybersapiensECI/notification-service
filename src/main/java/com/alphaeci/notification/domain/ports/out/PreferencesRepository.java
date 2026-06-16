package com.alphaeci.notification.domain.ports.out;

import java.util.UUID;

import com.alphaeci.notification.domain.model.NotificationPreferences;

import java.util.Optional;

public interface PreferencesRepository {
    NotificationPreferences save(NotificationPreferences preferences);
    Optional<NotificationPreferences> findByUserId(UUID userId);
}