package com.alphaeci.notification.infrastructure.adapters.persistence;

import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NotificationPersistenceMapper {

    @Mapping(target = "id", expression = "java(notification.getId() != null ? notification.getId().toString() : null)")
    @Mapping(target = "userId", expression = "java(notification.getUserId() != null ? notification.getUserId().toString() : null)")
    @Mapping(target = "referenceId", expression = "java(notification.getReferenceId() != null ? notification.getReferenceId().toString() : null)")
    NotificationDocument toDocument(Notification notification);

    @Mapping(target = "id", expression = "java(doc.getId() != null ? java.util.UUID.fromString(doc.getId()) : null)")
    @Mapping(target = "userId", expression = "java(doc.getUserId() != null ? java.util.UUID.fromString(doc.getUserId()) : null)")
    @Mapping(target = "referenceId", expression = "java(doc.getReferenceId() != null ? java.util.UUID.fromString(doc.getReferenceId()) : null)")
    Notification toDomain(NotificationDocument doc);

    @Mapping(target = "id", expression = "java(prefs.getId() != null ? prefs.getId().toString() : null)")
    @Mapping(target = "userId", expression = "java(prefs.getUserId() != null ? prefs.getUserId().toString() : null)")
    NotificationPreferencesDocument toDocument(NotificationPreferences prefs);

    @Mapping(target = "id", expression = "java(doc.getId() != null ? java.util.UUID.fromString(doc.getId()) : null)")
    @Mapping(target = "userId", expression = "java(doc.getUserId() != null ? java.util.UUID.fromString(doc.getUserId()) : null)")
    NotificationPreferences toDomain(NotificationPreferencesDocument doc);

    @Mapping(target = "id", expression = "java(reminder.getId() != null ? reminder.getId().toString() : null)")
    @Mapping(target = "userId", expression = "java(reminder.getUserId() != null ? reminder.getUserId().toString() : null)")
    @Mapping(target = "eventId", expression = "java(reminder.getEventId() != null ? reminder.getEventId().toString() : null)")
    EventReminderDocument toDocument(EventReminder reminder);

    @Mapping(target = "id", expression = "java(doc.getId() != null ? java.util.UUID.fromString(doc.getId()) : null)")
    @Mapping(target = "userId", expression = "java(doc.getUserId() != null ? java.util.UUID.fromString(doc.getUserId()) : null)")
    @Mapping(target = "eventId", expression = "java(doc.getEventId() != null ? java.util.UUID.fromString(doc.getEventId()) : null)")
    EventReminder toDomain(EventReminderDocument doc);
}
