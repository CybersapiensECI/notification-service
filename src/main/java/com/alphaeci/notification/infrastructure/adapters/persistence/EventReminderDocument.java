package com.alphaeci.notification.infrastructure.adapters.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "eventReminders")
public class EventReminderDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String eventId;
    private LocalDateTime eventDate;
    private boolean reminded24h;
    private boolean reminded1h;
}
