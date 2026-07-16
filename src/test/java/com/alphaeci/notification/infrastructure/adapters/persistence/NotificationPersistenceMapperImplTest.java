package com.alphaeci.notification.infrastructure.adapters.persistence;

import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationPersistenceMapperImplTest {

    private final NotificationPersistenceMapper mapper = new NotificationPersistenceMapperImpl();

    // ---- Notification ----

    @Test
    void notification_toDocument_withValues_stringifiesUuids() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Notification n = Notification.builder()
                .id(id).userId(userId).type(NotificationType.PARCHE_MESSAGE)
                .channel(NotificationChannel.IN_APP).title("t").body("b")
                .read(true).referenceId(refId).createdAt(now).build();

        NotificationDocument d = mapper.toDocument(n);
        assertEquals(id.toString(), d.getId());
        assertEquals(userId.toString(), d.getUserId());
        assertEquals(refId.toString(), d.getReferenceId());
        assertEquals(NotificationType.PARCHE_MESSAGE, d.getType());
        assertTrue(d.isRead());
        assertEquals(now, d.getCreatedAt());
    }

    @Test
    void notification_toDocument_withNullUuids_leavesNulls() {
        Notification n = Notification.builder().title("t").build();
        NotificationDocument d = mapper.toDocument(n);
        assertNull(d.getId());
        assertNull(d.getUserId());
        assertNull(d.getReferenceId());
        assertEquals("t", d.getTitle());
    }

    @Test
    void notification_toDomain_withValues_parsesUuids() {
        UUID id = UUID.randomUUID();
        NotificationDocument d = NotificationDocument.builder()
                .id(id.toString()).userId(id.toString()).referenceId(id.toString())
                .type(NotificationType.OTP_VERIFICATION).channel(NotificationChannel.EMAIL)
                .title("t").body("b").read(false).createdAt(LocalDateTime.now()).build();
        Notification n = mapper.toDomain(d);
        assertEquals(id, n.getId());
        assertEquals(id, n.getUserId());
        assertEquals(id, n.getReferenceId());
        assertEquals(NotificationChannel.EMAIL, n.getChannel());
    }

    @Test
    void notification_toDomain_withNullStrings_leavesNulls() {
        NotificationDocument d = NotificationDocument.builder().title("t").build();
        Notification n = mapper.toDomain(d);
        assertNull(n.getId());
        assertNull(n.getUserId());
        assertNull(n.getReferenceId());
    }

    @Test
    void notification_nullArgs() {
        assertNull(mapper.toDocument((Notification) null));
        assertNull(mapper.toDomain((NotificationDocument) null));
    }

    // ---- Preferences ----

    @Test
    void preferences_roundTrip_withValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        NotificationPreferences p = NotificationPreferences.builder()
                .id(id).userId(id).connectionRequest(true).parcheMessage(true)
                .eventReminder(true).nearbyParche(false).achievementUnlocked(true)
                .parcheInvitation(false).updatedAt(now).build();

        NotificationPreferencesDocument d = mapper.toDocument(p);
        assertEquals(id.toString(), d.getId());
        assertEquals(id.toString(), d.getUserId());
        assertTrue(d.isConnectionRequest());
        assertFalse(d.isNearbyParche());
        assertEquals(now, d.getUpdatedAt());

        NotificationPreferences back = mapper.toDomain(d);
        assertEquals(id, back.getId());
        assertEquals(id, back.getUserId());
        assertTrue(back.isEventReminder());
    }

    @Test
    void preferences_withNullUuids() {
        NotificationPreferences p = NotificationPreferences.builder().connectionRequest(true).build();
        NotificationPreferencesDocument d = mapper.toDocument(p);
        assertNull(d.getId());
        assertNull(d.getUserId());

        NotificationPreferencesDocument doc = NotificationPreferencesDocument.builder()
                .parcheMessage(true).build();
        NotificationPreferences back = mapper.toDomain(doc);
        assertNull(back.getId());
        assertNull(back.getUserId());
        assertTrue(back.isParcheMessage());
    }

    @Test
    void preferences_nullArgs() {
        assertNull(mapper.toDocument((NotificationPreferences) null));
        assertNull(mapper.toDomain((NotificationPreferencesDocument) null));
    }

    // ---- EventReminder ----

    @Test
    void eventReminder_roundTrip_withValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now().plusHours(3);
        EventReminder r = EventReminder.builder()
                .id(id).userId(id).eventId(id).eventDate(date)
                .reminded24h(true).reminded1h(false).build();

        EventReminderDocument d = mapper.toDocument(r);
        assertEquals(id.toString(), d.getId());
        assertEquals(id.toString(), d.getUserId());
        assertEquals(id.toString(), d.getEventId());
        assertEquals(date, d.getEventDate());
        assertTrue(d.isReminded24h());

        EventReminder back = mapper.toDomain(d);
        assertEquals(id, back.getId());
        assertEquals(id, back.getUserId());
        assertEquals(id, back.getEventId());
        assertFalse(back.isReminded1h());
    }

    @Test
    void eventReminder_withNullUuids() {
        EventReminder r = EventReminder.builder().reminded1h(true).build();
        EventReminderDocument d = mapper.toDocument(r);
        assertNull(d.getId());
        assertNull(d.getUserId());
        assertNull(d.getEventId());

        EventReminderDocument doc = EventReminderDocument.builder().reminded24h(true).build();
        EventReminder back = mapper.toDomain(doc);
        assertNull(back.getId());
        assertNull(back.getUserId());
        assertNull(back.getEventId());
        assertTrue(back.isReminded24h());
    }

    @Test
    void eventReminder_nullArgs() {
        assertNull(mapper.toDocument((EventReminder) null));
        assertNull(mapper.toDomain((EventReminderDocument) null));
    }
}
