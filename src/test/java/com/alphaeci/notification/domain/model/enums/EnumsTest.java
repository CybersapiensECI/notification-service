package com.alphaeci.notification.domain.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void notificationType_valuesAndValueOf() {
        assertEquals(10, NotificationType.values().length);
        for (NotificationType t : NotificationType.values()) {
            assertEquals(t, NotificationType.valueOf(t.name()));
        }
        assertEquals(NotificationType.OTP_VERIFICATION, NotificationType.valueOf("OTP_VERIFICATION"));
    }

    @Test
    void notificationChannel_valuesAndValueOf() {
        assertEquals(2, NotificationChannel.values().length);
        assertEquals(NotificationChannel.IN_APP, NotificationChannel.valueOf("IN_APP"));
        assertEquals(NotificationChannel.EMAIL, NotificationChannel.valueOf("EMAIL"));
    }
}
