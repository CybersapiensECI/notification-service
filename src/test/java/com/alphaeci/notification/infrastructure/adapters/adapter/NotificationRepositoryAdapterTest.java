package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationDocument;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationMongoRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationRepositoryAdapterTest {

    @Mock private NotificationMongoRepository mongoRepository;
    @Mock private NotificationPersistenceMapper mapper;
    @Mock private MongoTemplate mongoTemplate;

    @InjectMocks private NotificationRepositoryAdapter adapter;

    @Test
    void save_mapsToDocumentAndBack() {
        Notification domain = Notification.builder().id(UUID.randomUUID()).build();
        NotificationDocument doc = NotificationDocument.builder().id("x").build();
        when(mapper.toDocument(domain)).thenReturn(doc);
        when(mongoRepository.save(doc)).thenReturn(doc);
        when(mapper.toDomain(doc)).thenReturn(domain);

        assertSame(domain, adapter.save(domain));
        verify(mongoRepository).save(doc);
    }

    @Test
    void findById_present() {
        UUID id = UUID.randomUUID();
        NotificationDocument doc = NotificationDocument.builder().id(id.toString()).build();
        Notification domain = Notification.builder().id(id).build();
        when(mongoRepository.findById(id.toString())).thenReturn(Optional.of(doc));
        when(mapper.toDomain(doc)).thenReturn(domain);

        Optional<Notification> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void findById_empty() {
        UUID id = UUID.randomUUID();
        when(mongoRepository.findById(id.toString())).thenReturn(Optional.empty());
        assertTrue(adapter.findById(id).isEmpty());
    }

    @Test
    void findAllByUserId_mapsPage() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 20);
        NotificationDocument doc = NotificationDocument.builder().userId(userId.toString()).build();
        Notification domain = Notification.builder().userId(userId).build();
        Page<NotificationDocument> page = new PageImpl<>(List.of(doc));
        when(mongoRepository.findAllByUserId(userId.toString(), pageable)).thenReturn(page);
        when(mapper.toDomain(doc)).thenReturn(domain);

        Page<Notification> result = adapter.findAllByUserId(userId, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(userId, result.getContent().get(0).getUserId());
    }

    @Test
    void countUnreadByUserId_delegates() {
        UUID userId = UUID.randomUUID();
        when(mongoRepository.countByUserIdAndReadFalse(userId.toString())).thenReturn(5L);
        assertEquals(5L, adapter.countUnreadByUserId(userId));
    }

    @Test
    void markAsRead_updatesFirst() {
        UUID id = UUID.randomUUID();
        adapter.markAsRead(id);
        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(NotificationDocument.class));
    }

    @Test
    void markAllAsRead_updatesMulti() {
        UUID userId = UUID.randomUUID();
        adapter.markAllAsRead(userId);
        verify(mongoTemplate).updateMulti(any(Query.class), any(Update.class), eq(NotificationDocument.class));
    }
}
