package com.alphaeci.notification.entrypoints.messaging.dto;

import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventsPojoTest {

    private final UUID id = UUID.randomUUID();

    @Test
    void authEvent() {
        AuthNotificationEvent e = AuthNotificationEvent.builder()
                .targetUserId(id).referenceId(id).type(NotificationType.OTP_VERIFICATION)
                .email("m@x.com").otp("123456").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getReferenceId());
        assertEquals(NotificationType.OTP_VERIFICATION, e.getType());
        assertEquals("m@x.com", e.getEmail());
        assertEquals("123456", e.getOtp());
        assertNull(new AuthNotificationEvent().getEmail());
        AuthNotificationEvent all =
                new AuthNotificationEvent(id, id, NotificationType.PASSWORD_RESET, "m@x.com", "999");
        assertEquals("999", all.getOtp());
    }

    @Test
    void achievementEvent() {
        AchievementNotificationEvent e = AchievementNotificationEvent.builder()
                .targetUserId(id).achievementId(id).achievementName("A").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getAchievementId());
        assertEquals("A", e.getAchievementName());
        assertNull(new AchievementNotificationEvent().getAchievementName());
        assertEquals("B", new AchievementNotificationEvent(id, id, "B").getAchievementName());
    }

    @Test
    void chatEvent() {
        ChatNotificationEvent e = ChatNotificationEvent.builder()
                .targetUserId(id).chatId(id).senderName("S").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getChatId());
        assertEquals("S", e.getSenderName());
        assertNull(new ChatNotificationEvent().getSenderName());
        assertEquals("T", new ChatNotificationEvent(id, id, "T").getSenderName());
    }

    @Test
    void eventEvent() {
        LocalDateTime date = LocalDateTime.now();
        EventNotificationEvent e = EventNotificationEvent.builder()
                .targetUserId(id).eventId(id).eventName("E").eventDate(date).build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getEventId());
        assertEquals("E", e.getEventName());
        assertEquals(date, e.getEventDate());
        assertNull(new EventNotificationEvent().getEventName());
        assertEquals(date, new EventNotificationEvent(id, id, "E", date).getEventDate());
    }

    @Test
    void geolocationEvent() {
        GeolocationNotificationEvent e = GeolocationNotificationEvent.builder()
                .targetUserId(id).parcheId(id).parcheName("P").location("L").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getParcheId());
        assertEquals("P", e.getParcheName());
        assertEquals("L", e.getLocation());
        assertNull(new GeolocationNotificationEvent().getLocation());
        assertEquals("L", new GeolocationNotificationEvent(id, id, "P", "L").getLocation());
    }

    @Test
    void hangoutEvent() {
        HangoutNotificationEvent e = HangoutNotificationEvent.builder()
                .targetUserId(id).parcheId(id).parcheName("P").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getParcheId());
        assertEquals("P", e.getParcheName());
        assertNull(new HangoutNotificationEvent().getParcheName());
        assertEquals("Q", new HangoutNotificationEvent(id, id, "Q").getParcheName());
    }

    @Test
    void matchingEvent() {
        MatchingNotificationEvent e = MatchingNotificationEvent.builder()
                .targetUserId(id).requesterId(id).requesterName("R").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getRequesterId());
        assertEquals("R", e.getRequesterName());
        assertNull(new MatchingNotificationEvent().getRequesterName());
        assertEquals("S", new MatchingNotificationEvent(id, id, "S").getRequesterName());
    }

    @Test
    void memberEvent() {
        MemberNotificationEvent e = MemberNotificationEvent.builder()
                .targetUserId(id).parcheId(id).memberName("M").parcheName("P").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getParcheId());
        assertEquals("M", e.getMemberName());
        assertEquals("P", e.getParcheName());
        assertNull(new MemberNotificationEvent().getMemberName());
        assertEquals("P", new MemberNotificationEvent(id, id, "M", "P").getParcheName());
    }

    @Test
    void profileEvent() {
        ProfileNotificationEvent e = ProfileNotificationEvent.builder()
                .targetUserId(id).reportId(id).reportName("R").build();
        assertEquals(id, e.getTargetUserId());
        assertEquals(id, e.getReportId());
        assertEquals("R", e.getReportName());
        assertNull(new ProfileNotificationEvent().getReportName());
        assertEquals("S", new ProfileNotificationEvent(id, id, "S").getReportName());
    }
}
