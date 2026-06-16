package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.ports.in.GetNotificationsUseCase;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNotificationsUseCaseImpl implements GetNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Page<NotificationResponse> execute(String userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable)
                .map(notificationMapper::toNotificationResponse);
    }
}