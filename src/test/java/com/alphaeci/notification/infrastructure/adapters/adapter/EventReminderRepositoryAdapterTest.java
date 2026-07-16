package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.infrastructure.adapters.persistence.EventReminderDocument;
import com.alphaeci.notification.infrastructure.adapters.persistence.EventReminderMongoRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventReminderRepositoryAdapterTest {

    @Mock private EventReminderMongoRepository mongoRepository;
    @Mock private NotificationPersistenceMapper mapper;

    @InjectMocks private EventReminderRepositoryAdapter adapter;

    @Test
    void save_roundTrip() {
        EventReminder domain = EventReminder.builder().id(UUID.randomUUID()).build();
        EventReminderDocument doc = EventReminderDocument.builder().id("x").build();
        when(mapper.toDocument(domain)).thenReturn(doc);
        when(mongoRepository.save(doc)).thenReturn(doc);
        when(mapper.toDomain(doc)).thenReturn(domain);

        assertSame(domain, adapter.save(domain));
    }

    @Test
    void findPendingReminders_mapsListAndPassesThresholds() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 1, 10, 0);
        EventReminderDocument doc = EventReminderDocument.builder().id("x").build();
        EventReminder domain = EventReminder.builder().id(UUID.randomUUID()).build();
        when(mongoRepository.findPendingReminders(now.plusHours(24), now.plusHours(1)))
                .thenReturn(List.of(doc));
        when(mapper.toDomain(doc)).thenReturn(domain);

        List<EventReminder> result = adapter.findPendingReminders(now);
        assertEquals(1, result.size());
        verify(mongoRepository).findPendingReminders(now.plusHours(24), now.plusHours(1));
    }

    @Test
    void findAllByUserId_mapsList() {
        UUID userId = UUID.randomUUID();
        EventReminderDocument doc = EventReminderDocument.builder().userId(userId.toString()).build();
        when(mongoRepository.findAllByUserId(userId.toString())).thenReturn(List.of(doc));
        when(mapper.toDomain(any(EventReminderDocument.class)))
                .thenReturn(EventReminder.builder().userId(userId).build());

        List<EventReminder> result = adapter.findAllByUserId(userId);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
    }

    @Test
    void findAllByUserId_emptyList() {
        UUID userId = UUID.randomUUID();
        when(mongoRepository.findAllByUserId(userId.toString())).thenReturn(List.of());
        assertTrue(adapter.findAllByUserId(userId).isEmpty());
    }
}
