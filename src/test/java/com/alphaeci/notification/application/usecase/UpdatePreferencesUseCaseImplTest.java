package com.alphaeci.notification.application.usecase;

import com.alphaeci.notification.application.dto.request.UpdatePreferencesRequest;
import com.alphaeci.notification.application.dto.response.PreferencesResponse;
import com.alphaeci.notification.application.mapper.NotificationMapper;
import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.ports.out.PreferencesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePreferencesUseCaseImplTest {

    @Mock private PreferencesRepository preferencesRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks private UpdatePreferencesUseCaseImpl useCase;

    @Test
    void execute_existingPreferences_updatesAndReturns() {
        UUID userId = UUID.randomUUID();

        NotificationPreferences existing = NotificationPreferences.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .connectionRequest(true)
                .updatedAt(LocalDateTime.now())
                .build();

        UpdatePreferencesRequest request = UpdatePreferencesRequest.builder()
                .connectionRequest(false)
                .parcheMessage(true)
                .eventReminder(true)
                .nearbyParche(false)
                .achievementUnlocked(true)
                .parcheInvitation(false)
                .build();

        NotificationPreferences saved = existing.toBuilder()
                .connectionRequest(false)
                .build();

        PreferencesResponse response = PreferencesResponse.builder()
                .userId(userId)
                .connectionRequest(false)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        when(preferencesRepository.save(any())).thenReturn(saved);
        when(notificationMapper.toPreferencesResponse(saved)).thenReturn(response);

        PreferencesResponse result = useCase.execute(userId, request);

        assertNotNull(result);
        assertFalse(result.isConnectionRequest());
        verify(preferencesRepository).save(any());
    }

    @Test
    void execute_noExistingPreferences_createsAndUpdates() {
        UUID userId = UUID.randomUUID();

        UpdatePreferencesRequest request = UpdatePreferencesRequest.builder()
                .connectionRequest(true)
                .parcheMessage(true)
                .build();

        NotificationPreferences saved = NotificationPreferences.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .connectionRequest(true)
                .build();

        PreferencesResponse response = PreferencesResponse.builder()
                .userId(userId)
                .connectionRequest(true)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(preferencesRepository.save(any())).thenReturn(saved);
        when(notificationMapper.toPreferencesResponse(saved)).thenReturn(response);

        PreferencesResponse result = useCase.execute(userId, request);

        assertNotNull(result);
        verify(preferencesRepository).save(any());
    }
}
