package com.tologram.repository;

import com.tologram.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    Page<Notification> findByRecipientUserIdOrderByCreatedAtDesc(String recipientUserId, Pageable pageable);
    
    List<Notification> findByRecipientUserIdAndReadStatusFalseOrderByCreatedAtDesc(String recipientUserId);
    
    long countByRecipientUserIdAndReadStatusFalse(String recipientUserId);
    
    void deleteByPostId(String postId);
    
    void deleteByRecipientUserIdAndSenderUserIdAndType(String recipientUserId, String senderUserId, Notification.NotificationType type);
}