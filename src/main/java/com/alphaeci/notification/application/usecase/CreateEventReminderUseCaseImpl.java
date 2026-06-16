package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.ports.in.CreateEventReminderUseCase;
import com.alphaeci.notification.domain.ports.out.EventReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateEventReminderUseCaseImpl implements CreateEventReminderUseCase {

    private final EventReminderRepository eventReminderRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public EventReminderResponse execute(String userId, CreateEventReminderRequest request) {
        EventReminder reminder = EventReminder.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .eventId(request.getEventId())
                .eventDate(request.getEventDate())
                .reminded24h(false)
                .reminded1h(false)
                .build();

        return notificationMapper.toEventReminderResponse(eventReminderRepository.save(reminder));
    }
}