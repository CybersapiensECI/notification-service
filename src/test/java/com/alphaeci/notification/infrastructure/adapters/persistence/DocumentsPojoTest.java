package com.alphaeci.notification.infrastructure.adapters.persistence;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DocumentsPojoTest {

    @Test
    void notificationDocument_allForms() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDocument d = NotificationDocument.builder()
                .id("id").userId("uid").type(NotificationType.PARCHE_MESSAGE)
                .channel(NotificationChannel.IN_APP).title("t").body("b")
                .read(true).referenceId("ref").createdAt(now).build();
        assertEquals("id", d.getId());
        assertEquals("uid", d.getUserId());
        assertEquals(NotificationType.PARCHE_MESSAGE, d.getType());
        assertEquals(NotificationChannel.IN_APP, d.getChannel());
        assertEquals("t", d.getTitle());
        assertEquals("b", d.getBody());
        assertTrue(d.isRead());
        assertEquals("ref", d.getReferenceId());
        assertEquals(now, d.getCreatedAt());

        assertNull(new NotificationDocument().getId());
        NotificationDocument all = new NotificationDocument("id", "uid",
                NotificationType.PARCHE_MESSAGE, NotificationChannel.EMAIL, "t", "b", false, "ref", now);
        assertEquals(NotificationChannel.EMAIL, all.getChannel());
    }

    @Test
    void eventReminderDocument_allForms() {
        LocalDateTime date = LocalDateTime.now();
        EventReminderDocument d = EventReminderDocument.builder()
                .id("id").userId("uid").eventId("eid").eventDate(date)
                .reminded24h(true).reminded1h(false).build();
        assertEquals("id", d.getId());
        assertEquals("uid", d.getUserId());
        assertEquals("eid", d.getEventId());
        assertEquals(date, d.getEventDate());
        assertTrue(d.isReminded24h());
        assertFalse(d.isReminded1h());

        EventReminderDocument copy = d.toBuilder().reminded1h(true).build();
        assertTrue(copy.isReminded1h());

        assertNull(new EventReminderDocument().getId());
        EventReminderDocument all = new EventReminderDocument("id", "uid", "eid", date, false, true);
        assertTrue(all.isReminded1h());
    }

    @Test
    void notificationPreferencesDocument_allForms() {
        LocalDateTime now = LocalDateTime.now();
        NotificationPreferencesDocument d = NotificationPreferencesDocument.builder()
                .id("id").userId("uid").connectionRequest(true).parcheMessage(true)
                .eventReminder(true).nearbyParche(true).achievementUnlocked(true)
                .parcheInvitation(true).updatedAt(now).build();
        assertEquals("id", d.getId());
        assertEquals("uid", d.getUserId());
        assertTrue(d.isConnectionRequest());
        assertTrue(d.isParcheMessage());
        assertTrue(d.isEventReminder());
        assertTrue(d.isNearbyParche());
        assertTrue(d.isAchievementUnlocked());
        assertTrue(d.isParcheInvitation());
        assertEquals(now, d.getUpdatedAt());

        assertNull(new NotificationPreferencesDocument().getId());
        NotificationPreferencesDocument all = new NotificationPreferencesDocument(
                "id", "uid", true, false, true, false, true, false, now);
        assertFalse(all.isParcheMessage());
    }
}
