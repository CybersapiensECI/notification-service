package com.patricia.notification.application.usecase;

import com.patricia.notification.application.dto.request.SendNotificationRequest;
import com.patricia.notification.application.mapper.NotificationMapper;
import com.patricia.notification.domain.exceptions.NotificationTypeDisabledException;
import com.patricia.notification.domain.model.Notification;
import com.patricia.notification.domain.model.NotificationPreferences;
import com.patricia.notification.domain.model.enums.NotificationType;
import com.patricia.notification.domain.ports.in.SendNotificationUseCase;
import com.patricia.notification.domain.ports.out.NotificationDeliveryPort;
import com.patricia.notification.domain.ports.out.NotificationRepository;
import com.patricia.notification.domain.ports.out.PreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendNotificationUseCaseImpl implements SendNotificationUseCase {

    private static final Set<NotificationType> CRITICAL_TYPES = Set.of(
            NotificationType.OTP_VERIFICATION,
            NotificationType.PASSWORD_RESET
    );

    private final NotificationRepository notificationRepository;
    private final NotificationDeliveryPort notificationDeliveryPort;
    private final PreferencesRepository preferencesRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void execute(SendNotificationRequest request) {
        if (!CRITICAL_TYPES.contains(request.getType())) {
            preferencesRepository.findByUserId(request.getUserId())
                    .ifPresent(prefs -> validateTypeEnabled(prefs, request.getType()));
        }

        Notification notification = notificationMapper.toNotification(request)
                .toBuilder()
                .id(UUID.randomUUID())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        notificationDeliveryPort.deliverRealTime(notification);

        if (CRITICAL_TYPES.contains(notification.getType())) {
            notificationDeliveryPort.deliverEmail(notification, request.getUserId().toString());
        }
    }

    private void validateTypeEnabled(NotificationPreferences prefs, NotificationType type) {
        boolean enabled = switch (type) {
            case CONNECTION_REQUEST -> prefs.isConnectionRequest();
            case PARCHE_MESSAGE -> prefs.isParcheMessage();
            case EVENT_REMINDER -> prefs.isEventReminder();
            case NEARBY_PARCHE -> prefs.isNearbyParche();
            case ACHIEVEMENT_UNLOCKED -> prefs.isAchievementUnlocked();
            case PARCHE_INVITATION -> prefs.isParcheInvitation();
            default -> true;
        };

        if (!enabled) {
            throw new NotificationTypeDisabledException(type);
        }
    }
}
