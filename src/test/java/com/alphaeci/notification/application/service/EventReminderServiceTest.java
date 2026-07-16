package com.alphaeci.notification.application.service;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.domain.ports.out.EventReminderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventReminderServiceTest {

    @Mock private EventReminderRepository eventReminderRepository;
    @Mock private SendNotificationUseCase sendNotificationUseCase;

    @InjectMocks private EventReminderService service;

    private EventReminder reminder(boolean r24, boolean r1, LocalDateTime date) {
        return EventReminder.builder()
                .id(UUID.randomUUID()).userId(UUID.randomUUID()).eventId(UUID.randomUUID())
                .eventDate(date).reminded24h(r24).reminded1h(r1).build();
    }

    @Test
    void processReminders_within1h_sendsBothWindows() {
        // eventDate in ~30 min: within24h true AND within1h true -> 2 notifications
        EventReminder r = reminder(false, false, LocalDateTime.now().plusMinutes(30));
        when(eventReminderRepository.findPendingReminders(any())).thenReturn(List.of(r));

        service.processReminders();

        ArgumentCaptor<SendNotificationRequest> captor =
                ArgumentCaptor.forClass(SendNotificationRequest.class);
        verify(sendNotificationUseCase, times(2)).execute(captor.capture());

        List<SendNotificationRequest> sent = captor.getAllValues();
        assertTrue(sent.stream().anyMatch(s -> s.getBody().contains("24 horas")));
        assertTrue(sent.stream().anyMatch(s -> s.getBody().contains("1 hora")));
        SendNotificationRequest first = sent.get(0);
        assertEquals(NotificationType.EVENT_REMINDER, first.getType());
        assertEquals(NotificationChannel.IN_APP, first.getChannel());
        assertEquals("Recordatorio de evento", first.getTitle());
        assertEquals(r.getUserId(), first.getUserId());
        assertEquals(r.getEventId(), first.getReferenceId());
    }

    @Test
    void processReminders_only24hWindow() {
        // eventDate in ~23h: within24h true, within1h false -> 1 notification
        EventReminder r = reminder(false, false, LocalDateTime.now().plusHours(23));
        when(eventReminderRepository.findPendingReminders(any())).thenReturn(List.of(r));

        service.processReminders();

        ArgumentCaptor<SendNotificationRequest> captor =
                ArgumentCaptor.forClass(SendNotificationRequest.class);
        verify(sendNotificationUseCase, times(1)).execute(captor.capture());
        assertTrue(captor.getValue().getBody().contains("24 horas"));
    }

    @Test
    void processReminders_only1hWindow_when24hAlreadySent() {
        // 24h already sent, eventDate in ~30 min -> only 1h notification
        EventReminder r = reminder(true, false, LocalDateTime.now().plusMinutes(30));
        when(eventReminderRepository.findPendingReminders(any())).thenReturn(List.of(r));

        service.processReminders();

        ArgumentCaptor<SendNotificationRequest> captor =
                ArgumentCaptor.forClass(SendNotificationRequest.class);
        verify(sendNotificationUseCase, times(1)).execute(captor.capture());
        assertTrue(captor.getValue().getBody().contains("1 hora"));
    }

    @Test
    void processReminders_allAlreadyReminded_sendsNothing() {
        EventReminder r = reminder(true, true, LocalDateTime.now().plusMinutes(30));
        when(eventReminderRepository.findPendingReminders(any())).thenReturn(List.of(r));

        service.processReminders();

        verify(sendNotificationUseCase, never()).execute(any());
    }

    @Test
    void processReminders_emptyList_sendsNothing() {
        when(eventReminderRepository.findPendingReminders(any())).thenReturn(List.of());
        service.processReminders();
        verify(sendNotificationUseCase, never()).execute(any());
    }
}
