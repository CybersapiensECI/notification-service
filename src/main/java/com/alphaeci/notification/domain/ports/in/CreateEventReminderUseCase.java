package com.alphaeci.notification.domain.ports.in;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;

public interface CreateEventReminderUseCase {
    EventReminderResponse execute(String userId, CreateEventReminderRequest request);
}