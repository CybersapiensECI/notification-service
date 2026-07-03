package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.exceptions.NotificationTypeDisabledException;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.out.NotificationDeliveryPort;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import com.alphaeci.notification.domain.ports.out.PreferencesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendNotificationUseCaseImplTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationDeliveryPort notificationDeliveryPort;
    @Mock private PreferencesRepository preferencesRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks private SendNotificationUseCaseImpl useCase;

    private UUID userId;
    private Notification notification;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(NotificationType.CONNECTION_REQUEST)
                .channel(NotificationChannel.IN_APP)
                .title("Test")
                .body("Test body")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_nonCritical_noPreferences_savesAndDeliversRealTime() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.CONNECTION_REQUEST)
                .channel(NotificationChannel.IN_APP)
                .title("Test")
                .body("Test body")
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(notificationMapper.toNotification(request)).thenReturn(notification);
        when(notificationRepository.save(any())).thenReturn(notification);

        useCase.execute(request);

        verify(notificationRepository).save(any());
        verify(notificationDeliveryPort).deliverRealTime(any());
        verify(notificationDeliveryPort, never()).deliverEmail(any(), any());
    }

    @Test
    void execute_nonCritical_typeDisabled_throwsException() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.CONNECTION_REQUEST)
                .channel(NotificationChannel.IN_APP)
                .title("Test")
                .body("Body")
                .build();

        NotificationPreferences prefs = NotificationPreferences.builder()
                .userId(userId)
                .connectionRequest(false)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.of(prefs));

        assertThrows(NotificationTypeDisabledException.class, () -> useCase.execute(request));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void execute_criticalType_skipsPreferencesCheck_sendsEmail() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.OTP_VERIFICATION)
                .channel(NotificationChannel.EMAIL)
                .title("OTP")
                .body("123456")
                .build();

        Notification otpNotification = notification.toBuilder()
                .type(NotificationType.OTP_VERIFICATION)
                .build();

        when(notificationMapper.toNotification(request)).thenReturn(otpNotification);
        when(notificationRepository.save(any())).thenReturn(otpNotification);

        useCase.execute(request);

        verify(preferencesRepository, never()).findByUserId(any());
        verify(notificationDeliveryPort).deliverRealTime(any());
        verify(notificationDeliveryPort).deliverEmail(any(), eq(userId.toString()));
    }

    @Test
    void execute_typeEnabled_savesAndDelivers() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.PARCHE_MESSAGE)
                .channel(NotificationChannel.IN_APP)
                .title("Mensaje")
                .body("Hola")
                .build();

        NotificationPreferences prefs = NotificationPreferences.builder()
                .userId(userId)
                .parcheMessage(true)
                .build();

        Notification parcheNotification = notification.toBuilder()
                .type(NotificationType.PARCHE_MESSAGE)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.of(prefs));
        when(notificationMapper.toNotification(request)).thenReturn(parcheNotification);
        when(notificationRepository.save(any())).thenReturn(parcheNotification);

        useCase.execute(request);

        verify(notificationRepository).save(any());
        verify(notificationDeliveryPort).deliverRealTime(any());
    }
}
