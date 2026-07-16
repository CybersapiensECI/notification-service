package com.alphaeci.notification.domain.exceptions;

import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void notFound_buildsMessage() {
        NotificationNotFoundException ex = new NotificationNotFoundException("abc-123");
        assertTrue(ex.getMessage().contains("abc-123"));
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void invalid_keepsMessage() {
        InvalidNotificationException ex = new InvalidNotificationException("bad payload");
        assertEquals("bad payload", ex.getMessage());
    }

    @Test
    void typeDisabled_buildsMessageFromType() {
        NotificationTypeDisabledException ex =
                new NotificationTypeDisabledException(NotificationType.PARCHE_MESSAGE);
        assertTrue(ex.getMessage().contains("PARCHE_MESSAGE"));
    }
}
