package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.model.Notification;
import com.alphaeci.notification.domain.ports.out.NotificationRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationDocument;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationMongoRepository;
import com.alphaeci.notification.infrastructure.adapters.persistence.NotificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationMongoRepository mongoRepository;
    private final NotificationPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Notification save(Notification notification) {
        NotificationDocument saved = mongoRepository.save(mapper.toDocument(notification));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return mongoRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    public Page<Notification> findAllByUserId(UUID userId, Pageable pageable) {
        return mongoRepository.findAllByUserId(userId.toString(), pageable).map(mapper::toDomain);
    }

    @Override
    public long countUnreadByUserId(UUID userId) {
        return mongoRepository.countByUserIdAndReadFalse(userId.toString());
    }

    @Override
    public void markAsRead(UUID notificationId) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(notificationId.toString())),
                Update.update("read", true),
                NotificationDocument.class
        );
    }

    @Override
    public void markAllAsRead(UUID userId) {
        mongoTemplate.updateMulti(
                Query.query(Criteria.where("userId").is(userId.toString()).and("read").is(false)),
                Update.update("read", true),
                NotificationDocument.class
        );
    }
}
