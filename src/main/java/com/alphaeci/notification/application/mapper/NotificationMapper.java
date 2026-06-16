package com.alphaeci.notification.application.mapper;

import com.alphaeci.notification.application.dto.request.CreateEventReminderRequest;
import com.alphaeci.notification.application.dto.request.SendNotificationRequest;
import com.alphaeci.notification.application.dto.response.EventReminderResponse;
import com.alphaeci.notification.application.dto.response.NotificationResponse;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.model.NotificationPreferences;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reminded24h", constant = "false")
    @Mapping(target = "reminded1h", constant = "false")
    EventReminder toEventReminder(CreateEventReminderRequest request, @org.mapstruct.Context String userId);

    EventReminderResponse toEventReminderResponse(EventReminder reminder);
}