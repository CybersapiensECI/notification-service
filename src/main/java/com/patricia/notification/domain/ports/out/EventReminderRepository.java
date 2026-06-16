package com.patricia.notification.domain.ports.out;

import com.patricia.notification.domain.model.EventReminder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventReminderRepository {
    EventReminder save(EventReminder reminder);
    List<EventReminder> findPendingReminders(LocalDateTime now);
    List<EventReminder> findAllByUserId(UUID userId);
}
