package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.application.dto.response.UnreadCountResponse;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllersTest {

    @Mock private GetNotificationsUseCase getNotificationsUseCase;
    @Mock private GetUnreadCountUseCase getUnreadCountUseCase;
    @Mock private MarkAsReadUseCase markAsReadUseCase;
    @Mock private GetPreferencesUseCase getPreferencesUseCase;
    @Mock private UpdatePreferencesUseCase updatePreferencesUseCase;
    @Mock private CreateEventReminderUseCase createEventReminderUseCase;
    @Mock private SendNotificationUseCase sendNotificationUseCase;

    private final UUID userId = UUID.randomUUID();

    @Test
    void notificationController_getNotifications() {
        NotificationController controller =
                new NotificationController(getNotificationsUseCase, getUnreadCountUseCase, markAsReadUseCase);
        Pageable pageable = PageRequest.of(0, 20);
        Page<NotificationResponse> page = new PageImpl<>(List.of(NotificationResponse.builder().build()));
        when(getNotificationsUseCase.execute(userId, pageable)).thenReturn(page);

        ResponseEntity<Page<NotificationResponse>> resp = controller.getNotifications(userId, pageable);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().getTotalElements());
    }

    @Test
    void notificationController_getUnreadCount() {
        NotificationController controller =
                new NotificationController(getNotificationsUseCase, getUnreadCountUseCase, markAsReadUseCase);
        UnreadCountResponse count = UnreadCountResponse.builder().userId(userId).unreadCount(3).build();
        when(getUnreadCountUseCase.execute(userId)).thenReturn(count);

        ResponseEntity<UnreadCountResponse> resp = controller.getUnreadCount(userId);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(3, resp.getBody().getUnreadCount());
    }

    @Test
    void notificationController_markOneAsRead() {
        NotificationController controller =
                new NotificationController(getNotificationsUseCase, getUnreadCountUseCase, markAsReadUseCase);
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> resp = controller.markOneAsRead(userId, id);
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(markAsReadUseCase).markOne(userId, id);
    }

    @Test
    void notificationController_markAllAsRead() {
        NotificationController controller =
                new NotificationController(getNotificationsUseCase, getUnreadCountUseCase, markAsReadUseCase);

        ResponseEntity<Void> resp = controller.markAllAsRead(userId);
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(markAsReadUseCase).markAll(userId);
    }

    @Test
    void preferencesController_get() {
        PreferencesController controller =
                new PreferencesController(getPreferencesUseCase, updatePreferencesUseCase);
        PreferencesResponse prefs = PreferencesResponse.builder().userId(userId).build();
        when(getPreferencesUseCase.execute(userId)).thenReturn(prefs);

        ResponseEntity<PreferencesResponse> resp = controller.getPreferences(userId);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(userId, resp.getBody().getUserId());
    }

    @Test
    void preferencesController_update() {
        PreferencesController controller =
                new PreferencesController(getPreferencesUseCase, updatePreferencesUseCase);
        UpdatePreferencesRequest req = UpdatePreferencesRequest.builder().parcheMessage(true).build();
        PreferencesResponse prefs = PreferencesResponse.builder().userId(userId).parcheMessage(true).build();
        when(updatePreferencesUseCase.execute(userId, req)).thenReturn(prefs);

        ResponseEntity<PreferencesResponse> resp = controller.updatePreferences(userId, req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().isParcheMessage());
    }

    @Test
    void eventReminderController_create() {
        EventReminderController controller = new EventReminderController(createEventReminderUseCase);
        CreateEventReminderRequest req = CreateEventReminderRequest.builder()
                .eventId(UUID.randomUUID()).build();
        EventReminderResponse response = EventReminderResponse.builder().userId(userId).build();
        when(createEventReminderUseCase.execute(userId, req)).thenReturn(response);

        ResponseEntity<EventReminderResponse> resp = controller.createReminder(userId, req);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(userId, resp.getBody().getUserId());
    }

    @Test
    void sendNotificationController_send() {
        SendNotificationController controller = new SendNotificationController(sendNotificationUseCase);
        SendNotificationRequest req = SendNotificationRequest.builder()
                .userId(userId).type(NotificationType.PARCHE_MESSAGE)
                .channel(NotificationChannel.IN_APP).title("t").body("b").build();

        ResponseEntity<Void> resp = controller.send(req);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        verify(sendNotificationUseCase).execute(req);
    }
}
