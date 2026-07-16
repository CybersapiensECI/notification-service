package com.alphaeci.notification.application.mapper;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperImplTest {

    private final NotificationMapper mapper = new NotificationMapperImpl();

    @Test
    void toNotification_mapsFields_andForcesDefaults() {
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();
        SendNotificationRequest req = SendNotificationRequest.builder()
                .userId(userId).type(NotificationType.PARCHE_INVITATION)
                .channel(NotificationChannel.IN_APP).title("t").body("b")
                .referenceId(refId).recipientEmail("m@x.com").build();

        Notification n = mapper.toNotification(req);

        assertEquals(userId, n.getUserId());
        assertEquals(NotificationType.PARCHE_INVITATION, n.getType());
        assertEquals(NotificationChannel.IN_APP, n.getChannel());
        assertEquals("t", n.getTitle());
        assertEquals("b", n.getBody());
        assertEquals(refId, n.getReferenceId());
        assertNull(n.getId());
        assertFalse(n.isRead());
        assertNull(n.getCreatedAt());
    }

    @Test
    void toNotification_null() {
        assertNull(mapper.toNotification(null));
    }

    @Test
    void toNotificationResponse_maps() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Notification n = Notification.builder()
                .id(id).userId(id).type(NotificationType.EVENT_REMINDER)
                .channel(NotificationChannel.EMAIL).title("t").body("b")
                .read(true).referenceId(id).createdAt(now).build();
        NotificationResponse r = mapper.toNotificationResponse(n);
        assertEquals(id, r.getId());
        assertEquals(NotificationType.EVENT_REMINDER, r.getType());
        assertTrue(r.isRead());
        assertEquals(now, r.getCreatedAt());
        assertNull(mapper.toNotificationResponse(null));
    }

    @Test
    void toPreferencesResponse_maps() {
        UUID id = UUID.randomUUID();
        NotificationPreferences p = NotificationPreferences.builder()
                .userId(id).connectionRequest(true).parcheMessage(true).build();
        PreferencesResponse r = mapper.toPreferencesResponse(p);
        assertEquals(id, r.getUserId());
        assertTrue(r.isConnectionRequest());
        assertTrue(r.isParcheMessage());
        assertNull(mapper.toPreferencesResponse(null));
    }

    @Test
    void toEventReminderResponse_maps() {
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        EventReminder e = EventReminder.builder()
                .id(id).userId(id).eventId(id).eventDate(date)
                .reminded24h(true).reminded1h(false).build();
        EventReminderResponse r = mapper.toEventReminderResponse(e);
        assertEquals(id, r.getId());
        assertEquals(date, r.getEventDate());
        assertTrue(r.isReminded24h());
        assertNull(mapper.toEventReminderResponse(null));
    }
}
