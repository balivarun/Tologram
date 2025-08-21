package com.tologram.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {
    
    @Id
    private String id;
    
    @NotNull(message = "Post ID is required")
    @Indexed
    private String postId;
    
    @NotNull(message = "User ID is required")
    @Indexed
    private String userId;
    
    @NotBlank(message = "Comment text is required")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String text;
    
    private String parentCommentId; // For nested comments
    
    @CreatedDate
    @Indexed
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
}