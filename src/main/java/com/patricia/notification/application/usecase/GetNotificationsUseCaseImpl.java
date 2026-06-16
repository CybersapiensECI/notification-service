package com.patricia.notification.application.usecase;

import com.patricia.notification.application.dto.response.NotificationResponse;
import com.patricia.notification.application.mapper.NotificationMapper;
import com.patricia.notification.domain.ports.in.GetNotificationsUseCase;
import com.patricia.notification.domain.ports.out.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetNotificationsUseCaseImpl implements GetNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Page<NotificationResponse> execute(UUID userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable)
                .map(notificationMapper::toNotificationResponse);
    }
}
