package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.ports.out.NotificationDeliveryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDeliveryAdapter implements NotificationDeliveryPort {

    private final SimpMessagingTemplate messagingTemplate;
    private final JavaMailSender mailSender;

    @Override
    public void deliverRealTime(Notification notification) {
        messagingTemplate.convertAndSendToUser(
                notification.getUserId().toString(),
                "/queue/notifications",
                notification
        );
    }

    @Override
    public void deliverEmail(Notification notification, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(notification.getTitle());
        message.setText(notification.getBody());
        mailSender.send(message);
    }
}
