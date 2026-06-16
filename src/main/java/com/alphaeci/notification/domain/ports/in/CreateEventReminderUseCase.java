package com.alphaeci.notification.domain.ports.in;

import java.util.UUID;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;

public interface CreateEventReminderUseCase {
    EventReminderResponse execute(UUID userId, CreateEventReminderRequest request);
}