package com.tologram.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {
    
    @Id
    private String id;
    
    @NotNull(message = "Recipient user ID is required")
    @Indexed
    private String recipientUserId;
    
    @NotNull(message = "Sender user ID is required")
    private String senderUserId;
    
    @NotNull(message = "Notification type is required")
    private NotificationType type;
    
    private String postId; // Optional, for like/comment notifications
    
    private String message;
    
    private boolean readStatus = false;
    
    @CreatedDate
    @Indexed
    private Instant createdAt;
    
    public enum NotificationType {
        LIKE,
        COMMENT,
        FOLLOW,
        MENTION
    }
}