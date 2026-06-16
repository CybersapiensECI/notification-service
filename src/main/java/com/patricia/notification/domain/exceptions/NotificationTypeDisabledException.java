package com.patricia.notification.domain.exceptions;

import com.patricia.notification.domain.model.enums.NotificationType;

public class NotificationTypeDisabledException extends RuntimeException {

    public NotificationTypeDisabledException(NotificationType type) {
        super("Notification type disabled by user: " + type.name());
    }
}
