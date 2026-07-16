package com.alphaeci.notification.domain.model;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelsTest {

    @Test
    void notification_builderAndGetters() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Notification n = Notification.builder()
                .id(id)
                .userId(userId)
                .type(NotificationType.PARCHE_INVITATION)
                .channel(NotificationChannel.IN_APP)
                .title("t")
                .body("b")
                .read(true)
                .referenceId(refId)
                .createdAt(now)
                .build();

        assertEquals(id, n.getId());
        assertEquals(userId, n.getUserId());
        assertEquals(NotificationType.PARCHE_INVITATION, n.getType());
        assertEquals(NotificationChannel.IN_APP, n.getChannel());
        assertEquals("t", n.getTitle());
        assertEquals("b", n.getBody());
        assertTrue(n.isRead());
        assertEquals(refId, n.getReferenceId());
        assertEquals(now, n.getCreatedAt());
    }

    @Test
    void notification_toBuilderCopiesAndOverrides() {
        Notification base = Notification.builder().title("a").read(false).build();
        Notification copy = base.toBuilder().read(true).build();
        assertEquals("a", copy.getTitle());
        assertTrue(copy.isRead());
    }

    @Test
    void notification_noArgsConstructor() {
        Notification n = new Notification();
        assertNull(n.getId());
        assertFalse(n.isRead());
    }

    @Test
    void notification_allArgsConstructor() {
        UUID id = UUID.randomUUID();
        Notification n = new Notification(id, id, NotificationType.OTP_VERIFICATION,
                NotificationChannel.EMAIL, "t", "b", false, id, LocalDateTime.now());
        assertEquals(id, n.getId());
        assertEquals(NotificationChannel.EMAIL, n.getChannel());
    }

    @Test
    void notificationPreferences_allAccessors() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        NotificationPreferences p = NotificationPreferences.builder()
                .id(id)
                .userId(id)
                .connectionRequest(true)
                .parcheMessage(true)
                .eventReminder(true)
                .nearbyParche(true)
                .achievementUnlocked(true)
                .parcheInvitation(true)
                .updatedAt(now)
                .build();

        assertEquals(id, p.getId());
        assertEquals(id, p.getUserId());
        assertTrue(p.isConnectionRequest());
        assertTrue(p.isParcheMessage());
        assertTrue(p.isEventReminder());
        assertTrue(p.isNearbyParche());
        assertTrue(p.isAchievementUnlocked());
        assertTrue(p.isParcheInvitation());
        assertEquals(now, p.getUpdatedAt());

        NotificationPreferences copy = p.toBuilder().connectionRequest(false).build();
        assertFalse(copy.isConnectionRequest());

        NotificationPreferences empty = new NotificationPreferences();
        assertFalse(empty.isEventReminder());

        NotificationPreferences all = new NotificationPreferences(
                id, id, true, false, true, false, true, false, now);
        assertTrue(all.isConnectionRequest());
        assertFalse(all.isParcheMessage());
    }

    @Test
    void eventReminder_allAccessors() {
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now().plusHours(2);
        EventReminder r = EventReminder.builder()
                .id(id)
                .userId(id)
                .eventId(id)
                .eventDate(date)
                .reminded24h(true)
                .reminded1h(false)
                .build();

        assertEquals(id, r.getId());
        assertEquals(id, r.getUserId());
        assertEquals(id, r.getEventId());
        assertEquals(date, r.getEventDate());
        assertTrue(r.isReminded24h());
        assertFalse(r.isReminded1h());

        EventReminder copy = r.toBuilder().reminded1h(true).build();
        assertTrue(copy.isReminded1h());

        EventReminder empty = new EventReminder();
        assertFalse(empty.isReminded24h());

        EventReminder all = new EventReminder(id, id, id, date, false, true);
        assertTrue(all.isReminded1h());
    }
}
