package com.alphaeci.notification.infrastructure.adapters.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {

    Page<NotificationDocument> findAllByUserId(String userId, Pageable pageable);

    long countByUserIdAndReadFalse(String userId);

    @Query("{ 'id': ?0 }")
    void markAsReadById(String id);
}
