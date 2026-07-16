package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDeliveryAdapterTest {

    @Mock private SimpMessagingTemplate messagingTemplate;
    @Mock private JavaMailSender mailSender;

    @InjectMocks private NotificationDeliveryAdapter adapter;

    @Test
    void deliverRealTime_sendsToUserQueue() {
        UUID userId = UUID.randomUUID();
        Notification n = Notification.builder().userId(userId).title("t").body("b").build();

        adapter.deliverRealTime(n);

        verify(messagingTemplate).convertAndSendToUser(
                userId.toString(), "/queue/notifications", n);
    }

    @Test
    void deliverEmail_buildsAndSendsMessage() {
        Notification n = Notification.builder().title("Subject").body("Content").build();

        adapter.deliverEmail(n, "user@x.com");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();
        assertArrayEquals(new String[]{"user@x.com"}, msg.getTo());
        assertEquals("Subject", msg.getSubject());
        assertEquals("Content", msg.getText());
    }
}
