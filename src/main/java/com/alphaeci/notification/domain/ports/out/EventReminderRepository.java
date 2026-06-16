package com.alphaeci.notification.domain.ports.out;

import java.util.UUID;

import com.alphaeci.notification.domain.model.EventReminder;

import java.time.LocalDateTime;
import java.util.List;

public interface EventReminderRepository {
    EventReminder save(EventReminder reminder);
    List<EventReminder> findPendingReminders(LocalDateTime now);
    List<EventReminder> findAllByUserId(UUID userId);
}