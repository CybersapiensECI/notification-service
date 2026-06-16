package com.alphaeci.notification.entrypoints.advice;

import com.alphaeci.notification.domain.exceptions.InvalidNotificationException;
import com.alphaeci.notification.domain.exceptions.NotificationNotFoundException;
import com.alphaeci.notification.domain.exceptions.NotificationTypeDisabledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotificationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage()));
    }

    @ExceptionHandler(InvalidNotificationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalid(InvalidNotificationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(ex.getMessage()));
    }

    @ExceptionHandler(NotificationTypeDisabledException.class)
    public ResponseEntity<Void> handleTypeDisabled(NotificationTypeDisabledException ex) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(message));
    }

    private Map<String, Object> errorBody(String message) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "message", message
        );
    }
}
