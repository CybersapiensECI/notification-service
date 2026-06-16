package com.patricia.notification.application.usecase;

import com.patricia.notification.application.dto.response.PreferencesResponse;
import com.patricia.notification.application.mapper.NotificationMapper;
import com.patricia.notification.domain.model.NotificationPreferences;
import com.patricia.notification.domain.ports.in.GetPreferencesUseCase;
import com.patricia.notification.domain.ports.out.PreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPreferencesUseCaseImpl implements GetPreferencesUseCase {

    private final PreferencesRepository preferencesRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public PreferencesResponse execute(UUID userId) {
        NotificationPreferences prefs = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
        return notificationMapper.toPreferencesResponse(prefs);
    }

    private NotificationPreferences createDefaultPreferences(UUID userId) {
        NotificationPreferences defaults = NotificationPreferences.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .connectionRequest(true)
                .parcheMessage(true)
                .eventReminder(true)
                .nearbyParche(true)
                .achievementUnlocked(true)
                .parcheInvitation(true)
                .updatedAt(LocalDateTime.now())
                .build();
        return preferencesRepository.save(defaults);
    }
}
