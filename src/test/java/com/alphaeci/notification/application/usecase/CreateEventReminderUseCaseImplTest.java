package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.ports.out.EventReminderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventReminderUseCaseImplTest {

    @Mock private EventReminderRepository eventReminderRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks private CreateEventReminderUseCaseImpl useCase;

    @Test
    void execute_createsReminderWithCorrectFields() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        LocalDateTime eventDate = LocalDateTime.now().plusDays(2);

        CreateEventReminderRequest request = new CreateEventReminderRequest(eventId, eventDate);

        EventReminder saved = EventReminder.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .eventId(eventId)
                .eventDate(eventDate)
                .reminded24h(false)
                .reminded1h(false)
                .build();

        EventReminderResponse response = EventReminderResponse.builder()
                .id(saved.getId())
                .userId(userId)
                .eventId(eventId)
                .eventDate(eventDate)
                .reminded24h(false)
                .reminded1h(false)
                .build();

        when(eventReminderRepository.save(any())).thenReturn(saved);
        when(notificationMapper.toEventReminderResponse(saved)).thenReturn(response);

        EventReminderResponse result = useCase.execute(userId, request);

        assertNotNull(result);
        assertEquals(eventId, result.getEventId());
        assertFalse(result.isReminded24h());
        assertFalse(result.isReminded1h());
        verify(eventReminderRepository).save(any());
    }
}
