package com.patricia.notification.domain.ports.out;

import com.patricia.notification.domain.model.NotificationPreferences;

import java.util.Optional;
import java.util.UUID;

public interface PreferencesRepository {
    NotificationPreferences save(NotificationPreferences preferences);
    Optional<NotificationPreferences> findByUserId(UUID userId);
}
