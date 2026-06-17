package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.UnreadCountResponse;
import com.alphaeci.notification.domain.ports.in.GetNotificationsUseCase;
import com.alphaeci.notification.domain.ports.in.GetUnreadCountUseCase;
import com.alphaeci.notification.domain.ports.in.MarkAsReadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;
    private final GetUnreadCountUseCase getUnreadCountUseCase;
    private final MarkAsReadUseCase markAsReadUseCase;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @RequestHeader("X-User-Id") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(getNotificationsUseCase.execute(userId, pageable));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(getUnreadCountUseCase.execute(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markOneAsRead(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID id) {
        markAsReadUseCase.markOne(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("X-User-Id") UUID userId) {
        markAsReadUseCase.markAll(userId);
        return ResponseEntity.noContent().build();
    }
}
