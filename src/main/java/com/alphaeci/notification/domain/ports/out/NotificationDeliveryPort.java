package com.alphaeci.notification.domain.ports.out;

import com.alphaeci.notification.domain.model.Notification;

public interface NotificationDeliveryPort {
    void deliverRealTime(Notification notification);
    void deliverEmail(Notification notification, String email);
}