package com.patricia.notification.domain.ports.in;

import com.patricia.notification.application.dto.request.CreateEventReminderRequest;
import com.patricia.notification.application.dto.response.EventReminderResponse;

import java.util.UUID;

public interface CreateEventReminderUseCase {
    EventReminderResponse execute(UUID userId, CreateEventReminderRequest request);
}
