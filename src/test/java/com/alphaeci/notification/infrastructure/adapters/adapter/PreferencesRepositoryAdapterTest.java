package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPreferencesDocument;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPreferencesMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreferencesRepositoryAdapterTest {

    @Mock private NotificationPreferencesMongoRepository mongoRepository;
    @Mock private NotificationPersistenceMapper mapper;

    @InjectMocks private PreferencesRepositoryAdapter adapter;

    @Test
    void save_roundTrip() {
        NotificationPreferences domain = NotificationPreferences.builder().id(UUID.randomUUID()).build();
        NotificationPreferencesDocument doc = NotificationPreferencesDocument.builder().id("x").build();
        when(mapper.toDocument(domain)).thenReturn(doc);
        when(mongoRepository.save(doc)).thenReturn(doc);
        when(mapper.toDomain(doc)).thenReturn(domain);

        assertSame(domain, adapter.save(domain));
    }

    @Test
    void findByUserId_present() {
        UUID userId = UUID.randomUUID();
        NotificationPreferencesDocument doc =
                NotificationPreferencesDocument.builder().userId(userId.toString()).build();
        NotificationPreferences domain = NotificationPreferences.builder().userId(userId).build();
        when(mongoRepository.findByUserId(userId.toString())).thenReturn(Optional.of(doc));
        when(mapper.toDomain(doc)).thenReturn(domain);

        Optional<NotificationPreferences> result = adapter.findByUserId(userId);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void findByUserId_empty() {
        UUID userId = UUID.randomUUID();
        when(mongoRepository.findByUserId(userId.toString())).thenReturn(Optional.empty());
        assertTrue(adapter.findByUserId(userId).isEmpty());
    }
}
