package com.alphaeci.notification.infrastructure.adapters.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventReminderMongoRepository extends MongoRepository<EventReminderDocument, String> {

    List<EventReminderDocument> findAllByUserId(String userId);

    @Query("{ $or: [ " +
            "{ 'reminded24h': false, 'eventDate': { $lte: ?0 } }, " +
            "{ 'reminded1h': false, 'eventDate': { $lte: ?1 } } " +
            "] }")
    List<EventReminderDocument> findPendingReminders(LocalDateTime threshold24h, LocalDateTime threshold1h);
}
