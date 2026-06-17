package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.EventReminder;
import com.alphaeci.notification.domain.ports.out.EventReminderRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.EventReminderMongoRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventReminderRepositoryAdapter implements EventReminderRepository {

    private final EventReminderMongoRepository mongoRepository;
    private final NotificationPersistenceMapper mapper;

    @Override
    public EventReminder save(EventReminder reminder) {
        return mapper.toDomain(mongoRepository.save(mapper.toDocument(reminder)));
    }

    @Override
    public List<EventReminder> findPendingReminders(LocalDateTime now) {
        return mongoRepository.findPendingReminders(now.plusHours(24), now.plusHours(1))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventReminder> findAllByUserId(UUID userId) {
        return mongoRepository.findAllByUserId(userId.toString())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
