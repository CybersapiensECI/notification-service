package com.alphaeci.notification.infrastructure.adapters.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationPreferencesMongoRepository extends MongoRepository<NotificationPreferencesDocument, String> {

    Optional<NotificationPreferencesDocument> findByUserId(String userId);
}
