package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.domain.exceptions.NotificationNotFoundException;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkAsReadUseCaseImplTest {

    @Mock private NotificationRepository notificationRepository;

    @InjectMocks private MarkAsReadUseCaseImpl useCase;

    @Test
    void markOne_existingNotification_marksAsRead() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();

        Notification notification = Notification.builder()
                .id(notificationId)
                .userId(userId)
                .type(NotificationType.CONNECTION_REQUEST)
                .channel(NotificationChannel.IN_APP)
                .title("Test")
                .body("Body")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        useCase.markOne(userId, notificationId);

        verify(notificationRepository).markAsRead(notificationId);
    }

    @Test
    void markOne_notFound_throwsException() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () -> useCase.markOne(userId, notificationId));
        verify(notificationRepository, never()).markAsRead(any());
    }

    @Test
    void markAll_callsMarkAllAsRead() {
        UUID userId = UUID.randomUUID();

        useCase.markAll(userId);

        verify(notificationRepository).markAllAsRead(userId);
    }
}
