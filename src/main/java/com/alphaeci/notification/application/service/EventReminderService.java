package com.alphaeci.notification.application.service;

import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import com.alphaeci.notification.domain.ports.in.SendNotificationUseCase;
import com.alphaeci.notification.domain.ports.out.EventReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventReminderService {

    private final EventReminderRepository eventReminderRepository;
    private final SendNotificationUseCase sendNotificationUseCase;

    @Scheduled(fixedDelay = 600000)
    public void processReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<EventReminder> pending = eventReminderRepository.findPendingReminders(now);

        for (EventReminder reminder : pending) {
            boolean within24h = !reminder.isReminded24h()
                    && reminder.getEventDate().isBefore(now.plusHours(24));

            boolean within1h = !reminder.isReminded1h()
                    && reminder.getEventDate().isBefore(now.plusHours(1));

            if (within24h) {
                sendReminder(reminder, "Tu evento comienza en 24 horas");
            }

            if (within1h) {
                sendReminder(reminder, "Tu evento comienza en 1 hora");
            }
        }
    }

    private void sendReminder(EventReminder reminder, String body) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(reminder.getUserId())
                .type(NotificationType.EVENT_REMINDER)
                .channel(NotificationChannel.IN_APP)
                .title("Recordatorio de evento")
                .body(body)
                .referenceId(reminder.getEventId())
                .build();

        sendNotificationUseCase.execute(request);
    }
}