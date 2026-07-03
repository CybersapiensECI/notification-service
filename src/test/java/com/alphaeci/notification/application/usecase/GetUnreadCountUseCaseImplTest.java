package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.response.UnreadCountResponse;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUnreadCountUseCaseImplTest {

    @Mock private NotificationRepository notificationRepository;

    @InjectMocks private GetUnreadCountUseCaseImpl useCase;

    @Test
    void execute_returnsCorrectCount() {
        UUID userId = UUID.randomUUID();
        when(notificationRepository.countUnreadByUserId(userId)).thenReturn(5L);

        UnreadCountResponse result = useCase.execute(userId);

        assertEquals(userId, result.getUserId());
        assertEquals(5L, result.getUnreadCount());
    }

    @Test
    void execute_noUnread_returnsZero() {
        UUID userId = UUID.randomUUID();
        when(notificationRepository.countUnreadByUserId(userId)).thenReturn(0L);

        UnreadCountResponse result = useCase.execute(userId);

        assertEquals(0L, result.getUnreadCount());
    }
}
