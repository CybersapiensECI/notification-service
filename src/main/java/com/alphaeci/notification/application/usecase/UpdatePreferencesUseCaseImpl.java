package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.ports.in.UpdatePreferencesUseCase;
import com.alphaeci.notification.domain.ports.out.PreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdatePreferencesUseCaseImpl implements UpdatePreferencesUseCase {

    private final PreferencesRepository preferencesRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public PreferencesResponse execute(String userId, UpdatePreferencesRequest request) {
        NotificationPreferences existing = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> NotificationPreferences.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        NotificationPreferences updated = NotificationPreferences.builder()
                .id(existing.getId())
                .userId(userId)
                .connectionRequest(request.isConnectionRequest())
                .parcheMessage(request.isParcheMessage())
                .eventReminder(request.isEventReminder())
                .nearbyParche(request.isNearbyParche())
                .achievementUnlocked(request.isAchievementUnlocked())
                .parcheInvitation(request.isParcheInvitation())
                .updatedAt(LocalDateTime.now())
                .build();

        return notificationMapper.toPreferencesResponse(preferencesRepository.save(updated));
    }
}