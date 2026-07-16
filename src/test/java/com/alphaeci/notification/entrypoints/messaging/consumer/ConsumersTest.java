package com.alphaeci.notification.entrypoints.messaging.consumer;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.CreateEventReminderUseCase;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.entrypoints.messaging.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumersTest {

    @Mock private SendNotificationUseCase sendNotificationUseCase;
    @Mock private CreateEventReminderUseCase createEventReminderUseCase;

    private final UUID user = UUID.randomUUID();
    private final UUID ref = UUID.randomUUID();

    private SendNotificationRequest capture() {
        ArgumentCaptor<SendNotificationRequest> captor =
                ArgumentCaptor.forClass(SendNotificationRequest.class);
        verify(sendNotificationUseCase).execute(captor.capture());
        return captor.getValue();
    }

    @Test
    void auth_otp() {
        AuthNotificationEvent event = AuthNotificationEvent.builder()
                .targetUserId(user).referenceId(ref).type(NotificationType.OTP_VERIFICATION)
                .email("m@x.com").otp("123456").build();
        new AuthNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(user, r.getUserId());
        assertEquals(NotificationType.OTP_VERIFICATION, r.getType());
        assertEquals(NotificationChannel.EMAIL, r.getChannel());
        assertEquals("Código de verificación", r.getTitle());
        assertTrue(r.getBody().contains("123456"));
        assertEquals("m@x.com", r.getRecipientEmail());
        assertEquals(ref, r.getReferenceId());
    }

    @Test
    void auth_passwordReset() {
        AuthNotificationEvent event = AuthNotificationEvent.builder()
                .targetUserId(user).type(NotificationType.PASSWORD_RESET)
                .email("m@x.com").otp("999").build();
        new AuthNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals("Recuperación de contraseña", r.getTitle());
        assertTrue(r.getBody().contains("999"));
    }

    @Test
    void auth_defaultType() {
        AuthNotificationEvent event = AuthNotificationEvent.builder()
                .targetUserId(user).type(NotificationType.REPORT_READY)
                .email("m@x.com").otp(null).build();
        new AuthNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals("Notificación de autenticación", r.getTitle());
        assertEquals("Revisa tu cuenta", r.getBody());
    }

    @Test
    void achievement() {
        AchievementNotificationEvent event = AchievementNotificationEvent.builder()
                .targetUserId(user).achievementId(ref).achievementName("Master").build();
        new AchievementNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.ACHIEVEMENT_UNLOCKED, r.getType());
        assertEquals("Logro desbloqueado", r.getTitle());
        assertTrue(r.getBody().contains("Master"));
        assertEquals(ref, r.getReferenceId());
    }

    @Test
    void chat() {
        ChatNotificationEvent event = ChatNotificationEvent.builder()
                .targetUserId(user).chatId(ref).senderName("Ana").build();
        new ChatNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.PARCHE_MESSAGE, r.getType());
        assertTrue(r.getBody().contains("Ana"));
        assertEquals(ref, r.getReferenceId());
    }

    @Test
    void geolocation() {
        GeolocationNotificationEvent event = GeolocationNotificationEvent.builder()
                .targetUserId(user).parcheId(ref).parcheName("Estudio").location("Bloque A").build();
        new GeolocationNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.NEARBY_PARCHE, r.getType());
        assertTrue(r.getBody().contains("Estudio"));
        assertTrue(r.getBody().contains("Bloque A"));
    }

    @Test
    void hangout() {
        HangoutNotificationEvent event = HangoutNotificationEvent.builder()
                .targetUserId(user).parcheId(ref).parcheName("Cafe").build();
        new HangoutNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.PARCHE_INVITATION, r.getType());
        assertTrue(r.getBody().contains("Cafe"));
    }

    @Test
    void matching() {
        MatchingNotificationEvent event = MatchingNotificationEvent.builder()
                .targetUserId(user).requesterId(ref).requesterName("Luis").build();
        new MatchingNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.CONNECTION_REQUEST, r.getType());
        assertTrue(r.getBody().contains("Luis"));
    }

    @Test
    void member() {
        MemberNotificationEvent event = MemberNotificationEvent.builder()
                .targetUserId(user).parcheId(ref).memberName("Pedro").parcheName("Grupo").build();
        new MemberNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.MEMBER_LEFT, r.getType());
        assertTrue(r.getBody().contains("Pedro"));
        assertTrue(r.getBody().contains("Grupo"));
    }

    @Test
    void profile() {
        ProfileNotificationEvent event = ProfileNotificationEvent.builder()
                .targetUserId(user).reportId(ref).reportName("Anual").build();
        new ProfileNotificationConsumer(sendNotificationUseCase).consume(event);

        SendNotificationRequest r = capture();
        assertEquals(NotificationType.REPORT_READY, r.getType());
        assertTrue(r.getBody().contains("Anual"));
    }

    @Test
    void event_delegatesToCreateReminder() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        EventNotificationEvent event = EventNotificationEvent.builder()
                .targetUserId(user).eventId(ref).eventName("Concierto").eventDate(date).build();
        new EventNotificationConsumer(createEventReminderUseCase).consume(event);

        ArgumentCaptor<CreateEventReminderRequest> captor =
                ArgumentCaptor.forClass(CreateEventReminderRequest.class);
        verify(createEventReminderUseCase).execute(eq(user), captor.capture());
        assertEquals(ref, captor.getValue().getEventId());
        assertEquals(date, captor.getValue().getEventDate());
    }
}
