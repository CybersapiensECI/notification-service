package com.patricia.notification.domain.ports.out;

import com.patricia.notification.domain.model.Notification;

public interface NotificationDeliveryPort {
    void deliverRealTime(Notification notification);
    void deliverEmail(Notification notification, String email);
}
