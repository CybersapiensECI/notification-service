package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.NotificationPreferences;
import com.alphaeci.notification.domain.ports.out.PreferencesRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPreferencesMongoRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PreferencesRepositoryAdapter implements PreferencesRepository {

    private final NotificationPreferencesMongoRepository mongoRepository;
    private final NotificationPersistenceMapper mapper;

    @Override
    public NotificationPreferences save(NotificationPreferences preferences) {
        return mapper.toDomain(mongoRepository.save(mapper.toDocument(preferences)));
    }

    @Override
    public Optional<NotificationPreferences> findByUserId(UUID userId) {
        return mongoRepository.findByUserId(userId.toString()).map(mapper::toDomain);
    }
}
