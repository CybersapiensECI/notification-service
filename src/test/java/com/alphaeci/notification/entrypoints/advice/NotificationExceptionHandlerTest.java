package com.alphaeci.notification.entrypoints.advice;

import com.alphaeci.notification.domain.exceptions.InvalidNotificationException;
import com.alphaeci.notification.domain.exceptions.NotificationNotFoundException;
import com.alphaeci.notification.domain.exceptions.NotificationTypeDisabledException;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationExceptionHandlerTest {

    private final NotificationExceptionHandler handler = new NotificationExceptionHandler();

    @Test
    void handleNotFound_returns404WithBody() {
        ResponseEntity<Map<String, Object>> resp =
                handler.handleNotFound(new NotificationNotFoundException("abc"));
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertTrue(resp.getBody().get("message").toString().contains("abc"));
        assertNotNull(resp.getBody().get("timestamp"));
    }

    @Test
    void handleInvalid_returns400WithBody() {
        ResponseEntity<Map<String, Object>> resp =
                handler.handleInvalid(new InvalidNotificationException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("bad", resp.getBody().get("message"));
    }

    @Test
    void handleTypeDisabled_returns204() {
        ResponseEntity<Void> resp =
                handler.handleTypeDisabled(new NotificationTypeDisabledException(NotificationType.NEARBY_PARCHE));
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void handleValidation_usesFirstFieldError() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of(new FieldError("obj", "title", "must not be blank")));

        ResponseEntity<Map<String, Object>> resp = handler.handleValidation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("title: must not be blank", resp.getBody().get("message"));
    }

    @Test
    void handleValidation_noFieldErrors_fallsBack() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<Map<String, Object>> resp = handler.handleValidation(ex);
        assertEquals("Validation error", resp.getBody().get("message"));
    }
}
