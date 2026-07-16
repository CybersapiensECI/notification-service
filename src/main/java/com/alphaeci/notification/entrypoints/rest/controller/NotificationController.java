package com.alphaeci.notification.entrypoints.rest.controller;

import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.UnreadCountResponse;
import com.alphaeci.notification.domain.ports.in.GetNotificationsUseCase;
import com.alphaeci.notification.domain.ports.in.GetUnreadCountUseCase;
import com.alphaeci.notification.domain.ports.in.MarkAsReadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/notifications", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Notifications — Inbox", description = "Query and read state of a user's notifications")
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;
    private final GetUnreadCountUseCase getUnreadCountUseCase;
    private final MarkAsReadUseCase markAsReadUseCase;

    @Operation(
            summary = "List the user's notifications",
            description = "Returns the notifications of the authenticated user, paginated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of notifications"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed X-User-Id header")
    })
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(getNotificationsUseCase.execute(userId, pageable));
    }

    @Operation(
            summary = "Count unread notifications",
            description = "Returns how many notifications of the authenticated user are still unread.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unread count"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed X-User-Id header")
    })
    @GetMapping("/unread/count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(getUnreadCountUseCase.execute(userId));
    }

    @Operation(
            summary = "Mark one notification as read",
            description = "Marks a single notification as read. The notification must belong to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found or not owned by the user")
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markOneAsRead(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId,
            @Parameter(description = "Notification identifier", required = true,
                    example = "9e8d7c6b-5a4f-43e2-b1c0-9d8e7f6a5b4c")
            @PathVariable UUID id) {
        markAsReadUseCase.markOne(userId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Mark every notification as read",
            description = "Marks all notifications of the authenticated user as read in a single operation.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All notifications marked as read"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed X-User-Id header")
    })
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @Parameter(description = "Authenticated user, propagated by the API Gateway", required = true,
                    example = "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03")
            @RequestHeader("X-User-Id") UUID userId) {
        markAsReadUseCase.markAll(userId);
        return ResponseEntity.noContent().build();
    }
}
