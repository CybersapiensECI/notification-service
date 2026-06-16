package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.ports.in.GetPreferencesUseCase;
import com.alphaeci.notification.domain.ports.out.PreferencesRepository;
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
