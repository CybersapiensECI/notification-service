package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.domain.exceptions.NotificationNotFoundException;
import com.alphaeci.notification.domain.ports.in.MarkAsReadUseCase;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkAsReadUseCaseImpl implements MarkAsReadUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    public void markOne(String userId, String notificationId) {
        notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notificationRepository.markAsRead(notificationId);
    }

    @Override
    public void markAll(String userId) {
        notificationRepository.markAllAsRead(userId);
    }
}