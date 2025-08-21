package com.tologram.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    
    @Id
    private String id;
    
    @NotNull(message = "User is required")
    @Indexed
    private String userId;
    
    private String imageUrl;
    
    private String videoUrl;
    
    @Size(max = 2200, message = "Caption cannot exceed 2200 characters")
    private String caption;
    
    @Indexed
    private List<String> hashtags = new ArrayList<>();
    
    private Set<String> likes = new HashSet<>();
    
    private int commentsCount = 0;
    
    @CreatedDate
    @Indexed
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
    
    public int getLikesCount() {
        return likes != null ? likes.size() : 0;
    }
    
    public boolean isLikedBy(String userId) {
        return likes != null && likes.contains(userId);
    }
    
    public void addLike(String userId) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(userId);
    }
    
    public void removeLike(String userId) {
        if (likes != null) {
            likes.remove(userId);
        }
    }
    
    public void incrementCommentsCount() {
        this.commentsCount++;
    }
    
    public void decrementCommentsCount() {
        if (this.commentsCount > 0) {
            this.commentsCount--;
        }
    }
}