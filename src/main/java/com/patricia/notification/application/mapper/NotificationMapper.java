package com.patricia.notification.application.mapper;

import com.patricia.notification.application.dto.request.SendNotificationRequest;
import com.patricia.notification.application.dto.response.EventReminderResponse;
import com.patricia.notification.application.dto.response.NotificationResponse;
import com.patricia.notification.application.dto.response.PreferencesResponse;
import com.patricia.notification.domain.model.EventReminder;
import com.patricia.notification.domain.model.Notification;
import com.patricia.notification.domain.model.NotificationPreferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    Notification toNotification(SendNotificationRequest request);

    NotificationResponse toNotificationResponse(Notification notification);

    PreferencesResponse toPreferencesResponse(NotificationPreferences preferences);

    EventReminderResponse toEventReminderResponse(EventReminder reminder);
}
