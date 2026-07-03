package com.alphaeci.notification.application.usecase;

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
class GetPreferencesUseCaseImplTest {

    @Mock private PreferencesRepository preferencesRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks private GetPreferencesUseCaseImpl useCase;

    @Test
    void execute_existingPreferences_returnsResponse() {
        UUID userId = UUID.randomUUID();
        NotificationPreferences prefs = NotificationPreferences.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .connectionRequest(true)
                .parcheMessage(false)
                .updatedAt(LocalDateTime.now())
                .build();

        PreferencesResponse response = PreferencesResponse.builder()
                .userId(userId)
                .connectionRequest(true)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.of(prefs));
        when(notificationMapper.toPreferencesResponse(prefs)).thenReturn(response);

        PreferencesResponse result = useCase.execute(userId);

        assertNotNull(result);
        verify(preferencesRepository, never()).save(any());
    }

    @Test
    void execute_noPreferences_createsDefaultsAndReturns() {
        UUID userId = UUID.randomUUID();
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

        PreferencesResponse response = PreferencesResponse.builder()
                .userId(userId)
                .connectionRequest(true)
                .build();

        when(preferencesRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(preferencesRepository.save(any())).thenReturn(defaults);
        when(notificationMapper.toPreferencesResponse(defaults)).thenReturn(response);

        PreferencesResponse result = useCase.execute(userId);

        assertNotNull(result);
        verify(preferencesRepository).save(any());
    }
}
