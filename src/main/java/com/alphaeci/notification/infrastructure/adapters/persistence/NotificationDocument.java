package com.alphaeci.notification.infrastructure.adapters.persistence;

import com.alphaeci.notification.domain.model.enums.NotificationChannel;
import com.alphaeci.notification.domain.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class NotificationDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    private NotificationType type;
    private NotificationChannel channel;
    private String title;
    private String body;
    private boolean read;
    private String referenceId;
    private LocalDateTime createdAt;
}
