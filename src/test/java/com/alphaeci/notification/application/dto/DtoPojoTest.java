package com.alphaeci.notification.application.dto;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.application.dto.response.UnreadCountResponse;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DtoPojoTest {

    @Test
    void sendNotificationRequest_builderGetters() {
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();
        SendNotificationRequest r = SendNotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.OTP_VERIFICATION)
                .channel(NotificationChannel.EMAIL)
                .title("t")
                .body("b")
                .referenceId(refId)
                .recipientEmail("mail@x.com")
                .build();

        assertEquals(userId, r.getUserId());
        assertEquals(NotificationType.OTP_VERIFICATION, r.getType());
        assertEquals(NotificationChannel.EMAIL, r.getChannel());
        assertEquals("t", r.getTitle());
        assertEquals("b", r.getBody());
        assertEquals(refId, r.getReferenceId());
        assertEquals("mail@x.com", r.getRecipientEmail());
    }

    @Test
    void createEventReminderRequest_allForms() {
        UUID eventId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        CreateEventReminderRequest r = CreateEventReminderRequest.builder()
                .eventId(eventId).eventDate(date).build();
        assertEquals(eventId, r.getEventId());
        assertEquals(date, r.getEventDate());

        CreateEventReminderRequest empty = new CreateEventReminderRequest();
        assertNull(empty.getEventId());

        CreateEventReminderRequest all = new CreateEventReminderRequest(eventId, date);
        assertEquals(date, all.getEventDate());
    }

    @Test
    void updatePreferencesRequest_allForms() {
        UpdatePreferencesRequest r = UpdatePreferencesRequest.builder()
                .connectionRequest(true)
                .parcheMessage(true)
                .eventReminder(true)
                .nearbyParche(true)
                .achievementUnlocked(true)
                .parcheInvitation(true)
                .build();
        assertTrue(r.isConnectionRequest());
        assertTrue(r.isParcheMessage());
        assertTrue(r.isEventReminder());
        assertTrue(r.isNearbyParche());
        assertTrue(r.isAchievementUnlocked());
        assertTrue(r.isParcheInvitation());

        UpdatePreferencesRequest empty = new UpdatePreferencesRequest();
        assertFalse(empty.isConnectionRequest());

        UpdatePreferencesRequest all =
                new UpdatePreferencesRequest(true, false, true, false, true, false);
        assertTrue(all.isConnectionRequest());
        assertFalse(all.isParcheMessage());
    }

    @Test
    void notificationResponse_allForms() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        NotificationResponse r = NotificationResponse.builder()
                .id(id).userId(id).type(NotificationType.NEARBY_PARCHE)
                .channel(NotificationChannel.IN_APP).title("t").body("b")
                .read(true).referenceId(id).createdAt(now).build();
        assertEquals(id, r.getId());
        assertEquals(id, r.getUserId());
        assertEquals(NotificationType.NEARBY_PARCHE, r.getType());
        assertEquals(NotificationChannel.IN_APP, r.getChannel());
        assertEquals("t", r.getTitle());
        assertEquals("b", r.getBody());
        assertTrue(r.isRead());
        assertEquals(id, r.getReferenceId());
        assertEquals(now, r.getCreatedAt());

        assertNull(new NotificationResponse().getId());
        NotificationResponse all = new NotificationResponse(id, id,
                NotificationType.NEARBY_PARCHE, NotificationChannel.IN_APP, "t", "b", false, id, now);
        assertFalse(all.isRead());
    }

    @Test
    void preferencesResponse_allForms() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        PreferencesResponse r = PreferencesResponse.builder()
                .userId(id).connectionRequest(true).parcheMessage(true)
                .eventReminder(true).nearbyParche(true).achievementUnlocked(true)
                .parcheInvitation(true).updatedAt(now).build();
        assertEquals(id, r.getUserId());
        assertTrue(r.isConnectionRequest());
        assertTrue(r.isParcheMessage());
        assertTrue(r.isEventReminder());
        assertTrue(r.isNearbyParche());
        assertTrue(r.isAchievementUnlocked());
        assertTrue(r.isParcheInvitation());
        assertEquals(now, r.getUpdatedAt());

        assertNull(new PreferencesResponse().getUserId());
        PreferencesResponse all = new PreferencesResponse(id, true, false, true, false, true, false, now);
        assertTrue(all.isConnectionRequest());
    }

    @Test
    void eventReminderResponse_allForms() {
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        EventReminderResponse r = EventReminderResponse.builder()
                .id(id).userId(id).eventId(id).eventDate(date)
                .reminded24h(true).reminded1h(false).build();
        assertEquals(id, r.getId());
        assertEquals(id, r.getUserId());
        assertEquals(id, r.getEventId());
        assertEquals(date, r.getEventDate());
        assertTrue(r.isReminded24h());
        assertFalse(r.isReminded1h());

        assertNull(new EventReminderResponse().getId());
        EventReminderResponse all = new EventReminderResponse(id, id, id, date, false, true);
        assertTrue(all.isReminded1h());
    }

    @Test
    void unreadCountResponse_allForms() {
        UUID id = UUID.randomUUID();
        UnreadCountResponse r = UnreadCountResponse.builder().userId(id).unreadCount(7).build();
        assertEquals(id, r.getUserId());
        assertEquals(7, r.getUnreadCount());

        assertEquals(0, new UnreadCountResponse().getUnreadCount());
        UnreadCountResponse all = new UnreadCountResponse(id, 3);
        assertEquals(3, all.getUnreadCount());
    }
}
