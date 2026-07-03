package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetNotificationsUseCaseImplTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks private GetNotificationsUseCaseImpl useCase;

    @Test
    void execute_returnsPagedNotifications() {
        UUID userId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);

        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(NotificationType.CONNECTION_REQUEST)
                .channel(NotificationChannel.IN_APP)
                .title("Test")
                .body("Body")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(notification.getId())
                .userId(userId)
                .build();

        Page<Notification> page = new PageImpl<>(List.of(notification));

        when(notificationRepository.findAllByUserId(userId, pageable)).thenReturn(page);
        when(notificationMapper.toNotificationResponse(notification)).thenReturn(response);

        Page<NotificationResponse> result = useCase.execute(userId, pageable);

        assertEquals(1, result.getTotalElements());
        verify(notificationRepository).findAllByUserId(userId, pageable);
    }

    @Test
    void execute_emptyResult_returnsEmptyPage() {
        UUID userId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);

        when(notificationRepository.findAllByUserId(userId, pageable)).thenReturn(Page.empty());

        Page<NotificationResponse> result = useCase.execute(userId, pageable);

        assertTrue(result.isEmpty());
    }
}
