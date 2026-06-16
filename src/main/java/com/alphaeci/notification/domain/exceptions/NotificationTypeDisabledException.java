package com.alphaeci.notification.domain.exceptions;

import com.alphaeci.notification.domain.model.enums.NotificationType;

public class NotificationTypeDisabledException extends RuntimeException {

    public NotificationTypeDisabledException(NotificationType type) {
        super("Notification type disabled by user: " + type.name());
    }
}